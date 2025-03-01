package com.DormitoryBack.domain.member.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;

@Service
public class UserServiceExternal {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptedEmailService encryptedEmailService; 

    public Boolean isUserExist(Long userId){
        User user=userRepository.findById(userId).orElse(null); //repository에 새로운 메서드 선언 후 리펙터링해야함
        if(user==null){
            return false;
        }
        return true;
    }

    public String getUserEmail(Long userId){
        User user=userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        String userEmailhashed=user.getEncryptedEmail();
        String userEmail=encryptedEmailService.getOriginEmail(userEmailhashed);

        return userEmail;
    }
  
    public void saveUserProfileImage(Long userId,String imageName){
        User user=userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        if(user.getImageName()!=null){
            throw new RuntimeException("ImageAlreadyExist");
        }
        user.setImageName(imageName);
        userRepository.save(user);
    }

    public String deleteUserProfileImage(Long userId){
        User user=userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        if(user.getImageName()==null){
            throw new RuntimeException("ImageAlreadyNotExist");
        }
        String imageName=user.getImageName();
        user.setImageName(null);
        userRepository.save(user);

        return imageName;
    }

    public String getUserImageName(Long userId){
        User user=userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        String imageName=user.getImageName();
        return imageName;
    }

}
