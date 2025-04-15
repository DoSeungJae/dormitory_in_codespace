package com.DormitoryBack.domain.member.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.article.comment.domain.entity.OrphanComment;
import com.DormitoryBack.domain.article.comment.domain.service.CommentService;
import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.domain.entity.DeletedUser;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.DeletedUserRepository;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.exception.ErrorInfo;
import com.DormitoryBack.exception.ErrorType;
import com.DormitoryBack.exception.globalException.EntityNotFoundException;

@Service
public class UserServiceExternal {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeletedUserRepository deletedUserRepository;

    @Autowired
    private EncryptedEmailService encryptedEmailService; 

    @Autowired
    @Lazy //circular reference가 해결되긴함...
    private CommentService commentService;

    public Boolean isUserExist(Long userId){
        User user=userRepository.findById(userId).orElse(null); //repository에 새로운 메서드 선언 후 리펙터링해야함
        if(user==null){
            return false;
        }
        return true;
    }

    public Boolean isDeletedUserExist(Long userId){
        DeletedUser deletedUser=deletedUserRepository.findById(userId).orElse(null);
        if(deletedUser==null){
            return false;
        }
        return true;
    }

    public void checkDeletedUserExistsOrThrow(Long userId){
        Boolean exists=deletedUserRepository.existsById(userId);
        if(!exists){
            throw new EntityNotFoundException(new ErrorInfo(ErrorType.EntityNotFound, "DeletedUser를 찾지 못했습니다."));
        }
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
     
    public Long getUserIdFromNickname(String nickname){
        User user=userRepository.findByNickName(nickname);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        Long userId=user.getId();
        return userId;
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
    public User getDeletedVirtualUserFromOrphanComment(Long commentId){
        OrphanComment orphanComment=commentService.getOrphanComment(commentId);
        Long userId=orphanComment.getDeletedUser().getId();
        
        DeletedUser deletedUser=deletedUserRepository
            .findById(userId)
            .orElseThrow(()->new EntityNotFoundException(new ErrorInfo(ErrorType.EntityNotFound, "DeletedUser를 찾지 못했습니다.")));

        User user=User.builder()
            .id(deletedUser.getId())
            .encryptedEmail("-")
            .passWord("-")
            .nickName(null)
            .dormId(null)
            .provider(null)
            .imageName(null)
            .role(null)
            .build();
            
        return user;
    }
}
