package com.DormitoryBack.domain.member.domain.service;

import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import com.DormitoryBack.domain.group.domain.service.GroupService;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.dto.PasswordInitDTO;
import com.DormitoryBack.domain.member.domain.dto.UserLogInDTO;
import com.DormitoryBack.domain.member.domain.dto.UserRequestDTO;
import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.enums.DuplicatedType;
import com.DormitoryBack.domain.member.domain.enums.RoleType;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionService;
import com.DormitoryBack.module.crypt.PIEncryptor;
import com.DormitoryBack.module.crypt.PasswordEncryptor;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private GroupService groupService;

    @Autowired
    private EncryptedEmailService encryptedEmailService;

    @Autowired
    private PIEncryptor piEncryptor;

    @Autowired
    private RestrictionService restrictionService;

    /*constructor 문제 때문에 일시적으로 주석 처리함
    public List<UserResponseDTO> getAllUsers(){
        List<User> users=userRepository.findAll();
        if(users.isEmpty()){
            throw new RuntimeException("사용자가 존재하지 않습니다.");
        }
        List<UserResponseDTO> responseDTO = users.stream()
                .map(user -> new UserResponseDTO(user.getId(),encryptedEmailService.getOriginEmail(user.getEncryptedEmail()), user.getNickName(),user.getDormId()))
                .collect(Collectors.toList());

        return responseDTO;
    }
    */


    public UserResponseDTO getUser(Long usrId){
        User user=userRepository.findById(usrId).orElse(null);
        if(user==null){
            throw new IllegalArgumentException("해당 아이디에 대한 사용자가 존재하지 않습니다.");
        }
        UserResponseDTO responseDTO=UserResponseDTO.builder()
                .id(usrId)
                .eMail(encryptedEmailService.getOriginEmail(user.getEncryptedEmail()))
                .nickName(user.getNickName())
                .dormId(user.getDormId())
                .build();

        return responseDTO;
    }

    public UserResponseDTO getUserByNickName(String nickName) {
        User user=userRepository.findByNickName(nickName);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        UserResponseDTO responseDTO=UserResponseDTO.builder()
                .eMail(encryptedEmailService.getOriginEmail(user.getEncryptedEmail())) //보안상 부적절하지만 의존성 문제 때문에 유지중...
                .nickName(nickName)
                .dormId(user.getDormId())
                .build();

        return responseDTO;
    }

    public String getUserNickName(String token) {
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
        Long userId= tokenProvider.getUserIdFromToken(token);
        User user=userRepository.findById(userId).orElse(null);
        return user.getNickName();
    }

    public UserResponseDTO updateUser(Long usrId, UserRequestDTO dto){
        if(dto.getMail()!=null){
            throw new RuntimeException("EmailCannotBeChanged");
        }
        User user=userRepository.findById(usrId).orElse(null);
        if(user==null){
            throw new IllegalArgumentException("해당 아이디에 대한 사용자가 존재하지 않습니다.");
        }
        String confirm=dto.getConfirmPassword();
        if(confirm==null){
            throw new RuntimeException("ConfirmPasswordNotCorrect");
        }
        if(!passwordEncryptor.matchesPassword(confirm, user.getPassWord())){
            throw new RuntimeException("ConfirmPasswordNotCorrect");
        }
        user.update(dto);
        if(dto.getPassWord()!=null){
            String encryptedPassword=passwordEncryptor.encryptPassword(dto.getPassWord());
            user.setPassWord(encryptedPassword);
        }
        User saved=userRepository.save(user);

        UserResponseDTO responseDTO=UserResponseDTO.builder()
                .nickName(saved.getNickName())
                .dormId(user.getDormId())
                .build();

        return responseDTO;

    }

    public void makeNewUser(UserRequestDTO dto) {
        String encryptedEmail;
        String email=dto.getMail();
        String nickname=dto.getNickName();
        Long dormId=dto.getDormId();
        ProviderType provider=dto.getProvider();

        if(email==null){
            throw new IllegalArgumentException("EmailOmitted");
        }
        try{
            encryptedEmail=piEncryptor.hashify(email);
        }catch(NoSuchAlgorithmException e){
            return ;
        }
        User existingUserMailWithProvider = userRepository.findByEncryptedEmailAndProvider(encryptedEmail,provider);
        User existingUserNick = userRepository.findByNickName(nickname);
        if (existingUserMailWithProvider != null) {
            throw new IllegalArgumentException("이미 사용중인 메일입니다.");
            //throw new IllegalArgumentException("DuplicatedMail"); <- 이 코드로 변경 필요 
        }
        if(existingUserNick != null){
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
            //throw new IllegalArgumentException("DuplicatedNickname"); <- 이 코드로 변경 필요 
        }
        String encryptedPassword=passwordEncryptor.encryptPassword(dto.getPassWord());
        
        if(provider!=null){
            encryptedPassword="social account";
        }
        
        User user = User.builder()
                .encryptedEmail(encryptedEmail)
                .passWord(encryptedPassword) 
                .nickName(nickname)
                .dormId(dormId)
                .provider(provider)
                .role(RoleType.ROLE_USER)
                .build();  
        
        userRepository.save(user);
        try{
            encryptedEmailService.setNewEmailMap(dto.getMail(),encryptedEmail);
        }catch(RuntimeException e){
            if(!e.getMessage().equals("DuplicatedEmailMap")){
                throw new RuntimeException(e.getMessage());
            }
        }

        return ;
    }

    public void authenticateSocialLoginToken(UserRequestDTO request){
        String authToken=request.getAuthToken();
        String originEmail=piEncryptor.decrypt_AES256(authToken);
        String requestEmail=request.getMail();
        
        if(!originEmail.equals(requestEmail)){
            throw new RuntimeException("InvalidAuthenticationTokenOrEmail");
        }
        
        makeNewUser(request);
    }

    public User getSocialAccount(ProviderType provider, String email){ //external로?
        String encryptedEmail;
        try{
            encryptedEmail=piEncryptor.hashify(email);
        }catch(NoSuchAlgorithmException e){
            return null;
        }
        User socialAccount=userRepository.findByEncryptedEmailAndProvider(encryptedEmail,provider); 
        return socialAccount;
    }

    public String logIn(UserLogInDTO dto){
        String encryptedEmail;
        try{
            encryptedEmail=piEncryptor.hashify(dto.getEMail());
        }catch(NoSuchAlgorithmException e){
            return null;
        }
        User user=userRepository.findByEncryptedEmailAndProviderIsNull(encryptedEmail);
        if(user==null){
            throw new RuntimeException("로그인 정보가 올바르지 않습니다."); // IllegalArgumentException -> Run //예외 메시지 변경 필요
        }
        else if(!passwordEncryptor.matchesPassword(dto.getPassWord(), user.getPassWord())){
            throw new RuntimeException("로그인 정보가 올바르지 않습니다."); //예외 메시지 변경 필요
        }

        Object result=restrictionService.getIsRestricted(user.getId());
        if(result instanceof String){ 
            String message=(String)result;
            throw new RuntimeException(message);
        }
        
        String token=tokenProvider.createToken(user);
        return token;
    }

    public void deleteUser(Long usrId, UserRequestDTO dto){
        User target=userRepository.findById(usrId).orElse(null);
        if(target==null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        else if(!passwordEncryptor.matchesPassword(dto.getConfirmPassword(), target.getPassWord())){
            throw new RuntimeException("ConfirmPasswordNotCorrect");
        }
        else if(groupService.isBelongToAnywhere(usrId)){
            throw new RuntimeException("CannotDeleteUserWhileGrouping");
        }

        try{
            userRepository.delete(target);
            String encryptedEmail=target.getEncryptedEmail();
            long emailCount=userRepository.countByEncryptedEmail(encryptedEmail);
            if(emailCount==0){
                encryptedEmailService.deleteEmailMap(encryptedEmail); 
            }
        }catch(Exception e){
            log.info(e.getMessage());
        }finally{
            target=null;
        }
    }

    public DuplicatedType checkEmailDuplicated(String email,ProviderType provider){
        String encryptedEmail;
        try{
            encryptedEmail=piEncryptor.hashify(email);
        }catch(NoSuchAlgorithmException e){
            return null;
        }

        User existingUserMail;
        if(provider==null){
            existingUserMail=userRepository.findByEncryptedEmailAndProviderIsNull(encryptedEmail);
        }else{
            existingUserMail=userRepository.findByEncryptedEmailAndProvider(encryptedEmail, provider);
        }

        if(existingUserMail==null){
            return DuplicatedType.NONDUPLICATED;
        }
        return DuplicatedType.DUPLICATED;
    }

    public void initUserPassword(PasswordInitDTO request){
        String email=request.getEmail();
        String token=request.getEmailToken();
        if(!tokenProvider.validateToken(token)){
            throw new RuntimeException("InvalidToken");
        }
        String emailFromToken=tokenProvider.getUserEmailFromToken(token);
        if(!emailFromToken.equals(email)){
            throw new RuntimeException("InvalidEmail");
        }

        String encryptedEmail;
        try{
            encryptedEmail=piEncryptor.hashify(email);
        }catch(NoSuchAlgorithmException e){
            return ;
        }
        
        User user=userRepository.findByEncryptedEmailAndProviderIsNull(encryptedEmail);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }

        String newPassword=request.getNewPassword();
        if(newPassword==null){
            throw new RuntimeException("InvalidPassword");
        }
        String encryptedPassword=passwordEncryptor.encryptPassword(newPassword);
        user.setPassWord(encryptedPassword);
        userRepository.save(user);

        return ;
    }
}
