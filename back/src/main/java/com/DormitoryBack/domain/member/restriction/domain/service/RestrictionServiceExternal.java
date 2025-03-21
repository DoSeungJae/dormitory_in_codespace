package com.DormitoryBack.domain.member.restriction.domain.service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;
import com.DormitoryBack.domain.member.restriction.domain.repository.RestrictionRepository;
import com.DormitoryBack.module.TimeOptimizer;

@Service
public class RestrictionServiceExternal {

    @Autowired
    private RestrictionRepository restrictionRepository;

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
