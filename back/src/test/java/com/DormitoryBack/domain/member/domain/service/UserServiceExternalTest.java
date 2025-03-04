package com.DormitoryBack.domain.member.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceExternalTest {
    
    @InjectMocks
    private UserServiceExternal userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptedEmailService encryptedEmailService;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsUserExist_true(){
        Long validUserId=1L;
        User user=User.builder()
            .id(validUserId)
            .build();

        when(userRepository.findById(validUserId)).thenReturn(Optional.of(user));

        Boolean result=userService.isUserExist(validUserId);

        assertTrue(result);
        verify(userRepository, times(1)).findById(validUserId);
    }

    @Test
    public void testIsUserExist_false(){
        Long invalidUserId=2L;
        
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        Boolean result=userService.isUserExist(invalidUserId);

        assertFalse(result);
        verify(userRepository,times(1)).findById(invalidUserId);
    }

    @Test
    public void testSaveProfileImage(){
        Long validUserId=1L;
        String validImageName="image.png";
        User user=User.builder()
            .id(validUserId)
            .build();

        when(userRepository.findById(validUserId)).thenReturn(Optional.of(user));

        userService.saveUserProfileImage(validUserId, validImageName);

        assertEquals("image.png", user.getImageName());
        verify(userRepository,times(1)).findById(validUserId);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void testSaveProfileImage_UserNotFound(){
        Long invalidUserId=2L;
        String validImageName="image.png";
        
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.saveUserProfileImage(invalidUserId,validImageName);
        });

        assertEquals("UserNotFound", exception.getMessage());
        verify(userRepository,times(1)).findById(invalidUserId);
    }

    @Test
    public void testDeleteProfileImage(){
        Long validUserId=1L;
        String imageName="image.png";
        User user=User.builder()
            .id(validUserId)
            .imageName(imageName)
            .build();
        
        when(userRepository.findById(validUserId)).thenReturn(Optional.of(user));

        String deleteImageName=userService.deleteUserProfileImage(validUserId);

        assertEquals(imageName, deleteImageName);
        verify(userRepository, times(1)).findById(validUserId);
        verify(userRepository, times(1)).save(user);
    }

    
    @Test
    public void testDeleteProfileImage_UserNotFound(){
        Long invalidUserId=2L;
        User user=User.builder()
            .id(invalidUserId)
            .build();
        
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        RuntimeException exception=assertThrows(RuntimeException.class,()->{
            userService.deleteUserProfileImage(invalidUserId);
        });

        assertEquals("UserNotFound", exception.getMessage());
        verify(userRepository, times(1)).findById(invalidUserId);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testDeleteProfileImage_ImageAlreadyNotExist(){
        Long validUserId=1L;
        User user=User.builder()
            .id(validUserId)
            .imageName(null)
            .build();

        when(userRepository.findById(validUserId)).thenReturn(Optional.of(user));

        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            userService.deleteUserProfileImage(validUserId);
        });

        assertEquals("ImageAlreadyNotExist", exception.getMessage());
        verify(userRepository, times(1)).findById(validUserId);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testGetUserImageName(){
        Long validUserId=1L;

        User user=User.builder()
            .id(validUserId)
            .imageName("image.png")
            .build();

        when(userRepository.findById(validUserId)).thenReturn(Optional.of(user));

        String imageName=userService.getUserImageName(validUserId);
        assertEquals(imageName, user.getImageName());
        verify(userRepository, times(1)).findById(validUserId);
    }

    @Test
    public void testGetUserImageName_UserNotFound(){
        Long invalidUserId=2L;

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            userService.getUserImageName(invalidUserId);
        });

        assertEquals("UserNotFound", exception.getMessage());
        verify(userRepository,times(1)).findById(invalidUserId);
    }

    @Test
    public void testGetUserEmail(){
        Long validUserId=1L;
        String userEmailHashed="testhash@testhash.comhash";

        User user=User.builder()
            .id(validUserId)
            .encryptedEmail(userEmailHashed)
            .build();

        String userEmail="test@test.com";

        when(encryptedEmailService.getOriginEmail(userEmailHashed)).thenReturn(userEmail);
        when(userRepository.findById(validUserId)).thenReturn(Optional.of(user));

        String email=userService.getUserEmail(validUserId);

        assertEquals(userEmail, email);
        verify(userRepository, times(1)).findById(validUserId);
        verify(encryptedEmailService,times(1)).getOriginEmail(userEmailHashed);
    }

    @Test
    public void testGetUserEmail_UserNotFound(){
        Long invalidUserId=2L;
        String userEmailHashed="testhash@testhash.comhash";

        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            userService.getUserImageName(invalidUserId);
        });

        assertEquals("UserNotFound", exception.getMessage());
        verify(userRepository,times(1)).findById(invalidUserId);
        verify(encryptedEmailService,times(0)).getOriginEmail(userEmailHashed);
    }

    @Test
    public void testGetUserIdFromNickname(){
        String validUserNickname="validNickname";

        User user=User.builder()
            .id(1L)
            .nickName(validUserNickname)
            .build();
        
        when(userRepository.findByNickName(validUserNickname)).thenReturn(user);

        Long userId=userService.getUserIdFromNickname(validUserNickname);

        assertEquals(userId, user.getId());
        verify(userRepository,times(1)).findByNickName(validUserNickname);
    }

    @Test
    public void testGetUserIdFromNickname_UserNotFound(){
        String invalidUserNickname="invalidUserNickname";

        when(userRepository.findByNickName(invalidUserNickname)).thenReturn(null);

        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            userService.getUserIdFromNickname(invalidUserNickname);
        });

        assertEquals("UserNotFound", exception.getMessage());
        verify(userRepository,times(1)).findByNickName(invalidUserNickname);
    }


}
