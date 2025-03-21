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

    private RestrictionService restrictionService;

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
            String restrictionDetail=restrictionService.makeRestrictionDetail(userId);
            return restrictionDetail;
        }

        return isRestricted;
    }

    
}
