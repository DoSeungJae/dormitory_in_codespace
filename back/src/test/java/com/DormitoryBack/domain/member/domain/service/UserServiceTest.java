package com.DormitoryBack.domain.member.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.dto.PasswordInitDTO;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.enums.DuplicatedType;
import com.DormitoryBack.domain.member.domain.enums.RoleType;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionService;
import com.DormitoryBack.module.crypt.PIEncryptor;
import com.DormitoryBack.module.crypt.PasswordEncryptor;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private RestrictionService restrictionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PIEncryptor piEncryptor;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @Mock
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCheckEmailDuplicated_NonDuplicated_NonProvider(){
        String nonDuplicatedEmail="email@email.com";
        String encryptedEmail="encrytped@encrypted.encrypted";
        ProviderType provider=ProviderType.GOOGLE;

        try{
            when(piEncryptor.hashify(nonDuplicatedEmail)).thenReturn(encryptedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        when(userRepository.findByEncryptedEmailAndProviderIsNull(encryptedEmail)).thenReturn(null);

        DuplicatedType duplicatedType=userService.checkEmailDuplicated(nonDuplicatedEmail, null);

        assertEquals(DuplicatedType.NONDUPLICATED, duplicatedType);

        try{
            verify(piEncryptor,times(1)).hashify(nonDuplicatedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        verify(userRepository,times(1)).findByEncryptedEmailAndProviderIsNull(encryptedEmail);
        verify(userRepository,times(0)).findByEncryptedEmailAndProvider(encryptedEmail, provider);
    }

    @Test
    public void testCheckEmailDuplicated_NonDuplicated_Provider(){
        ProviderType provider=ProviderType.GOOGLE;
        String nonDuplicatedEmail="email@email.com";
        String encryptedEmail="encrytped@encrypted.encrypted";

        try{
            when(piEncryptor.hashify(nonDuplicatedEmail)).thenReturn(encryptedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        when(userRepository.findByEncryptedEmailAndProvider(encryptedEmail, provider)).thenReturn(null);

        DuplicatedType duplicatedType=userService.checkEmailDuplicated(nonDuplicatedEmail, provider);

        assertEquals(DuplicatedType.NONDUPLICATED, duplicatedType);
        try{
            verify(piEncryptor,times(1)).hashify(nonDuplicatedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        verify(userRepository,times(1)).findByEncryptedEmailAndProvider(encryptedEmail, provider);
        verify(userRepository,times(0)).findByEncryptedEmailAndProviderIsNull(encryptedEmail);
    }

    @Test
    public void testCheckEmailDuplicated_Duplicated(){
        String duplicatedEmail="email@email.com";
        String encryptedEmail="encrytped@encrypted.encrypted";
        User user=User.builder()
            .id(1L)
            .nickName("nickname")
            .passWord("password")
            .provider(null)
            .role(RoleType.ROLE_USER)
            .dormId(1L)
            .encryptedEmail("email")
            .build();

        try{
            when(piEncryptor.hashify(duplicatedEmail)).thenReturn(encryptedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        when(userRepository.findByEncryptedEmailAndProviderIsNull(encryptedEmail)).thenReturn(user);

        DuplicatedType duplicatedType=userService.checkEmailDuplicated(duplicatedEmail, null);

        assertEquals(DuplicatedType.DUPLICATED, duplicatedType);
        try{
            verify(piEncryptor,times(1)).hashify(duplicatedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        verify(userRepository,times(1)).findByEncryptedEmailAndProviderIsNull(encryptedEmail);
    }

    @Test
    public void testInitUserPassword_Success(){
        String validEmail="email@email.com";
        String validToken="validToken";
        String newValidPassword="validNewPassword";
        String encryptedPassword="encryptedPassword";
        String hashedEmail="hashed@hashed.com";

        User user=User.builder()
            .id(1L)
            .nickName("nickname")
            .passWord("password")
            .provider(null)
            .role(RoleType.ROLE_USER)
            .dormId(1L)
            .encryptedEmail(hashedEmail)
            .build();

        PasswordInitDTO request=PasswordInitDTO.builder()
            .email(validEmail)
            .emailToken(validToken)
            .newPassword(newValidPassword)
            .build();
        
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserEmailFromToken(validToken)).thenReturn(validEmail);
        try{
            when(piEncryptor.hashify(validEmail)).thenReturn(hashedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        when(passwordEncryptor.encryptPassword(newValidPassword)).thenReturn(encryptedPassword);
        when(userRepository.findByEncryptedEmailAndProviderIsNull(hashedEmail)).thenReturn(user);
        
        userService.initUserPassword(request);

        try{
            verify(piEncryptor,times(1)).hashify(validEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        assertEquals(encryptedPassword, user.getPassWord());
        verify(passwordEncryptor,times(1)).encryptPassword(newValidPassword); 
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void testInitUserPassword_InvalidPassword(){
        String newValidPassword="validNewPassword";
        String validEmail="email@email.com";
        String validToken="validToken";
        String hashedEmail="hashed@hashed.com";

        PasswordInitDTO request=PasswordInitDTO.builder()
            .email(validEmail)
            .emailToken(validToken)
            .newPassword(newValidPassword)
            .build();

        User user=User.builder()
            .id(1L)
            .nickName("nickname")
            .passWord("password")
            .provider(null)
            .role(RoleType.ROLE_USER)
            .dormId(1L)
            .encryptedEmail(hashedEmail)
            .build();
        
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserEmailFromToken(validToken)).thenReturn(validEmail);
        try{
            when(piEncryptor.hashify(validEmail)).thenReturn(hashedEmail);
        }catch(NoSuchAlgorithmException e){
            fail();
        }
        when(userRepository.findByEncryptedEmailAndProviderIsNull(hashedEmail)).thenReturn(user);


        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            userService.initUserPassword(request);
        });
        

        assertEquals("InvalidEmail",exception.getMessage());
        verify(passwordEncryptor,times(0)).encryptPassword(newValidPassword);
        verify(userRepository,times(0)).save(user);
    }

    
    @Test
    public void testInitUserPassword_InvalidEmail(){
        String validEmail="email@email.com";
        String invalidEmail="invalid@email.com";
        String validNewPassword="newPassword";
        String validToken="validToken";
        String hashedEmail="hashed@hashed.com";

        PasswordInitDTO request=PasswordInitDTO.builder()
            .email(invalidEmail)
            .emailToken(validToken)
            .newPassword(validNewPassword)
            .build();

        User user=User.builder()
            .id(1L)
            .nickName("nickname")
            .passWord("password")
            .provider(null)
            .role(RoleType.ROLE_USER)
            .dormId(1L)
            .encryptedEmail(hashedEmail)
            .build();
        
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserEmailFromToken(validToken)).thenReturn(validEmail);

        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            userService.initUserPassword(request);
        });
        
        assertEquals("InvalidEmail",exception.getMessage());
        verify(userRepository,times(0)).save(user);
    }
}
