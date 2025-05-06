package com.DormitoryBack.domain.member.domain.api;

import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import com.DormitoryBack.domain.member.domain.dto.PasswordInitDTO;
import com.DormitoryBack.domain.member.domain.dto.UserLogInDTO;
import com.DormitoryBack.domain.member.domain.dto.UserRequestDTO;
import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.domain.enums.DuplicatedType;
import com.DormitoryBack.domain.member.domain.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/user")
@Slf4j
public class UserApi {
    private final UserService userService;
    @Autowired
    public UserApi(UserService userService){
        this.userService=userService;

    }
    @GetMapping("/test")
    public String userTest(){
        log.info("12");
        return "12";
    }


    /*
    @GetMapping("")
    public ResponseEntity allUsers(){
        List<UserResponseDTO> users=userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);

    }
    */

    @GetMapping("/{usrId}")
    public ResponseEntity<UserResponseDTO> user(@PathVariable("usrId") Long usrId){
        UserResponseDTO responseDTO=userService.getUser(usrId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/findByNick/{nickName}")
    public ResponseEntity<UserResponseDTO> userByNickName(@PathVariable("nickName") String nickName){
        UserResponseDTO responseDTO=userService.getUserByNickName(nickName);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

    }

    @GetMapping("/NickName")
    public ResponseEntity<String> userNickName(@RequestHeader("Authorization") String token){
        String userNickName=userService.getUserNickName(token);
        return ResponseEntity.status(HttpStatus.OK).body(userNickName);
    }

    @GetMapping("/emailDuplicated")
    public ResponseEntity<DuplicatedType> emailDuplicated(@RequestHeader("Email") String email, @RequestParam(required=false) ProviderType provider){
        DuplicatedType isDuplicated=userService.checkEmailDuplicated(email,provider);
        return ResponseEntity.status(HttpStatus.OK).body(isDuplicated);
    }

    
    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody UserLogInDTO dto){ //return type : ResponseEntity <String>
        try{
            String token=userService.logIn(dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(token);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(false);
        }
    }


    @PostMapping("/join")
    public ResponseEntity<String> signUp(@RequestBody UserRequestDTO dto) {
        userService.makeNewUser(dto);
        return ResponseEntity.ok().body("회원가입이 성공적으로 처리되었습니다");
    }

    @PostMapping("/join/socialAccount")
    public ResponseEntity<Void> signUpWithSocialAccount(@RequestBody UserRequestDTO request){
        userService.authenticateSocialLoginToken(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //토큰 인증 필요
    @PatchMapping("/{usrId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("usrId") Long usrId, @RequestBody UserRequestDTO dto){
        UserResponseDTO responseDTO=userService.updateUser(usrId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PatchMapping("/initPassword")
    public ResponseEntity<Void> initUserPasword(@RequestBody PasswordInitDTO request){
        userService.initUserPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{usrId}")
    public ResponseEntity<String> deleteUser(@PathVariable("usrId") Long usrId, @RequestBody UserRequestDTO dto){
        userService.deleteUser(usrId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("UserDeleted");
    }



}
