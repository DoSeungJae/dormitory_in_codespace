package com.DormitoryBack.domain.member.restriction.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionRequestDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTOList;
import com.DormitoryBack.domain.member.restriction.domain.repository.RestrictionRepository;
import com.DormitoryBack.module.TimeOptimizer;
import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;
import com.DormitoryBack.domain.member.restriction.domain.enums.Function;

@Service
public class RestrictionService {

    //@Value("${administrator.key}") //value 어노테이션을 사용하면 test 환경에서 exception이 thorwn.
    private final String key="20220393-2470011192"; //런타임에서도 Value 어노테이션이 동작하지 않는지 확인하기
    //임시 코드, 수정 반드시 필요함.

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    public RestrictionResponseDTOList getMyRestrictions(String token){

        Long userId=tokenProvider.getUserIdFromToken(token);
        List<Restriction> restrictions=restrictionRepository.findAllByUserId(userId); //테스트가 필요하긴함.
        RestrictionResponseDTOList dtoList=this.makeDTOList(restrictions);

        return dtoList;
    }

    public RestrictionResponseDTO restrict(RestrictionRequestDTO dto){
        if(!key.equals(dto.getAccessKey())){
            throw new RuntimeException("KeyNotCorrect");
        }
        User user=userRepository.findById(dto.getUserId()).orElse(null);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        Restriction restriction=Restriction.builder()
            .userId(dto.getUserId())
            .reason(dto.getReason())
            .durationDays(dto.getDurationDays())
            .triggeredTime(TimeOptimizer.now())
            .build();

        List<Function> suspendedFunctions=dto.getSuspensionFunctionsAsEnum();
        setSuspendedFunctions(suspendedFunctions,restriction);
        Restriction saved=restrictionRepository.save(restriction);
        return this.makeDTO(saved);
    }

    public void setSuspendedFunctions(List<Function> functions, Restriction restriction){
        Iterator<Function> iterator=functions.iterator();
        while (iterator.hasNext()){
            Function function=iterator.next();
            restriction.suspendFunction(function);
        }
    }
    //위 메서드는 restrict 메서드에서만 호출되므로 restrict 메서드의 테스트가 검증되었다면 테스트가 필요없음.

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
        LocalDateTime expireDate=restriction.getTriggeredTime().plusDays(restriction.getDurationDays());
        LocalDateTime now=TimeOptimizer.now();
        Boolean isExpired=now.isAfter(expireDate) || now.equals(expireDate);
        
        RestrictionResponseDTO dto=RestrictionResponseDTO.builder()
            .userId(restriction.getUserId())
            .expireTime(expireDate)
            .isExpired(isExpired)
            .reason(restriction.getReason())
            .suspendedFunctions(restriction.getSuspendedFunctionsAsStringList())
            .build();
        
        return dto;
    }

    public Object getIsRestricted(Function function, Long userId){
        List<Restriction> restrictions=restrictionRepository.findAllByUserId(userId);
        Iterator<Restriction> iterator=restrictions.iterator();
        Boolean isRestricted=false;
        while(iterator.hasNext()){
            Restriction restriction=iterator.next();
            Boolean functionSuspended=restriction.isSuspended(function); //제제가 지정된 기능에 대한 제제인지 확인.
            if(!functionSuspended){ 
                continue; //지정된 기능에 대한 제제가 아니라면 고려할 필요 없으므로 넘어감; not restricted
            }
            LocalDateTime expireDate=restriction.getTriggeredTime().plusDays(restriction.getDurationDays());
            LocalDateTime now=TimeOptimizer.now();
            Boolean isExpired=now.isAfter(expireDate) || now.equals(expireDate);
            if(isExpired){ //제제가 만료되었는지 확인, 만료된 제제라면 고려할 필요 없음; not restricted
                continue;  
            }
            //지정된 기능에 대한 제제이며 아직 유효한 제제 -> restricted 판정. 이런 것이 "하나라도 있으면" 해당 기능은 제제된 것임.
            isRestricted=true;
            break;
        }

        if(function==Function.LOGIN && isRestricted){
            return makeLoginRestrictionDetail(userId);
        }
        return isRestricted;
    }

    //!
    public String makeLoginRestrictionDetail(Long userId){
        String message="LoginFunctionRestricted:expiredAt:";
        LocalDateTime expiredTime=getExpiredTime(userId,Function.LOGIN);
        message=message+expiredTime.toString();

        return message;
    }

    //!
    public LocalDateTime getExpiredTime(Long userId, Function function){
        List<Restriction> restrictions=restrictionRepository.findAllByUserId(userId);
        Iterator<Restriction> iterator=restrictions.iterator();
        LocalDateTime entireExpieredTime=TimeOptimizer.now();
        while(iterator.hasNext()){
            Restriction restriction=iterator.next();
            LocalDateTime expiredTime=restriction.getTriggeredTime().plusDays(restriction.getDurationDays());
            if(expiredTime.isAfter(entireExpieredTime)){
                entireExpieredTime=expiredTime;
            }
        }
        return entireExpieredTime;
    }
    
    
}
