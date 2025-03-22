package com.DormitoryBack.domain.member.restriction.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.auth.email.domain.service.EmailService;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionRequestDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTOList;
import com.DormitoryBack.domain.member.restriction.domain.repository.RestrictionRepository;
import com.DormitoryBack.module.TimeOptimizer;
import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;
@Service
public class RestrictionService {

    @Value("${administrator.key}") //value 어노테이션을 사용하면 test 환경에서 exception이 thorwn.
    private String key; //런타임에서도 Value 어노테이션이 동작하지 않는지 확인하기

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserServiceExternal userService;

    @Autowired
    private EmailService emailService;

    public RestrictionResponseDTOList getMyRestrictions(String token){

        Long userId=tokenProvider.getUserIdFromToken(token);
        List<Restriction> restrictions=restrictionRepository.findAllByUserId(userId); 
        RestrictionResponseDTOList dtoList=this.makeDTOList(restrictions);

        return dtoList;
    }

    public RestrictionResponseDTO restrict(RestrictionRequestDTO dto){ //restrict에서는 durationDays가 0이 될 수 없음.
        if(!key.equals(dto.getAccessKey())){
            throw new RuntimeException("KeyNotCorrect");
        }

        Long userId=dto.getUserId();
        Boolean userExist=userService.isUserExist(userId);
        if(!userExist){
            throw new RuntimeException("UserNotFound");
        }
        String userEmail=userService.getUserEmail(userId);
        Long durationDays=dto.getDurationDays();
        if(durationDays==0L){
            throw new RuntimeException("DurationDaysMustBeMoreThan0");
        }
        Restriction restriction=Restriction.builder()
            .userId(dto.getUserId())
            .reason(dto.getReason())
            .durationDays(durationDays)
            .triggeredTime(TimeOptimizer.now())
            .build();
        
        Restriction saved=restrictionRepository.save(restriction);
        RestrictionResponseDTO responseDTO=makeDTO(saved); 
        
        emailService.sendRestrictionConfirmMail(userEmail, responseDTO); 

        return responseDTO;
    }

    public RestrictionResponseDTO warn(RestrictionRequestDTO request){
        String keyInput=request.getAccessKey();
        if(!key.equals(keyInput)){
            throw new RuntimeException("KeyNotCorrect");
        }

        Long userId=request.getUserId();
        Boolean userExist=userService.isUserExist(userId);
        if(!userExist){
            throw new RuntimeException("UserNotFound");
        }
        Long durationDays=request.getDurationDays();
        if(durationDays!=0L){
            throw new IllegalArgumentException("DurationDaysMustBe0");
        }

        Restriction restriction=Restriction.builder()
            .userId(request.getUserId())
            .reason(request.getReason())
            .durationDays(durationDays) 
            .triggeredTime(TimeOptimizer.now())
            .build();
        
        Restriction saved=restrictionRepository.save(restriction);
        RestrictionResponseDTO dto=makeDTO(saved);
        return dto;      
    }



    public RestrictionResponseDTOList makeDTOList(List<Restriction> restrictions){
        List<RestrictionResponseDTO> list=new ArrayList<>();
        Iterator<Restriction> iterator=restrictions.iterator();

        while(iterator.hasNext()){
            Restriction restriction = iterator.next();
            RestrictionResponseDTO dto=this.makeDTO(restriction);
            list.add(dto);
        }
        
        RestrictionResponseDTOList dtoList=RestrictionResponseDTOList.builder()
            .dtoList(list)
            .number(list.size())
            .build();

        return dtoList;
    }

    public RestrictionResponseDTO makeDTO(Restriction restriction){
        Long durationDays=restriction.getDurationDays();
        Boolean isExpired=null;
        LocalDateTime expireDate=null;
        if(durationDays!=0L){
            expireDate=restriction.getTriggeredTime().plusDays(restriction.getDurationDays());
        }
        LocalDateTime now=TimeOptimizer.now();
        if(expireDate!=null){
            isExpired=now.isAfter(expireDate) || now.equals(expireDate);
        }    
        RestrictionResponseDTO dto=RestrictionResponseDTO.builder()
            .userId(restriction.getUserId())
            .expireTime(expireDate) //경고일 경우 null이 되어야함.
            .isExpired(isExpired) //경고일 경우 null이 되어야함.
            .reason(restriction.getReason())
            .build();
        
        return dto;
    }

    public Object getIsRestricted(Long userId){
        List<Restriction> restrictions=restrictionRepository.findAllByUserId(userId);
        Iterator<Restriction> iterator=restrictions.iterator();
        Boolean isRestricted=false;
        while(iterator.hasNext()){
            Restriction restriction=iterator.next();
            if(restriction.getDurationDays()==null){ 
                continue;
            }
            LocalDateTime expireDate=restriction.getTriggeredTime().plusDays(restriction.getDurationDays());
            LocalDateTime now=TimeOptimizer.now();
            Boolean isExpired=now.isAfter(expireDate) || now.equals(expireDate);
            if(isExpired){
                continue;  
            }
            isRestricted=true;
            break;
        }

        if(isRestricted){
            String restrictionDetail=makeRestrictionDetail(userId);
            return restrictionDetail;
        }

        return isRestricted;
    }

    public Boolean getIsRestricted(String token){
        if(!tokenProvider.validateToken(token)){
            throw new RuntimeException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        List<Restriction> restrictions=restrictionRepository.findAllByUserId(userId);
        Iterator<Restriction> iterator=restrictions.iterator();
        Boolean isRestricted=false;
        while(iterator.hasNext()){
            Restriction restriction=iterator.next();
            if(restriction.getDurationDays()==null){
                continue;
            }
            LocalDateTime expireDate=restriction.getTriggeredTime().plusDays(restriction.getDurationDays());
            LocalDateTime now=TimeOptimizer.now();
            Boolean isExpired=now.isAfter(expireDate) || now.equals(expireDate);
            if(isExpired){
                continue;  
            }
            isRestricted=true;
            break;
        }
        return isRestricted;
    }

    public String makeRestrictionDetail(Long userId){
        String message="LoginRestricted:expiredAt:";
        LocalDateTime expiredTime=getExpiredTime(userId);
        message=message+expiredTime.toString();

        return message;
    }
 

    public LocalDateTime getExpiredTime(Long userId){
        List<Restriction> restrictions=restrictionRepository.findAllByUserId(userId);
        Iterator<Restriction> iterator=restrictions.iterator();
        LocalDateTime entireExpieredTime=TimeOptimizer.now();
        while(iterator.hasNext()){
            Restriction restriction=iterator.next();
            Long durationDays=restriction.getDurationDays();
            if(durationDays==null){
                continue;
            }
            LocalDateTime expiredTime=restriction.getTriggeredTime().plusDays(durationDays);
            if(expiredTime.isAfter(entireExpieredTime)){
                entireExpieredTime=expiredTime;
            }
        }
        return entireExpieredTime;
    }
    
    
}
