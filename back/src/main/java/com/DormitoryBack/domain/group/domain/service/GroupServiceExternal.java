package com.DormitoryBack.domain.group.domain.service;

import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;

import io.jsonwebtoken.JwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceExternal {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RedisTemplate<String,Long> redisTemplate;

    public Long getGroupState(Long groupId){
        //마감,종료, 진행에 관한 상태만 반환, 0L : 그룹 없음, 1L : 진행 중, -1L : 마감됨, -2L :종료됨
        Group group=groupRepository.findById(groupId).orElse(null);
        if(group==null){
            return 0L;
        }
        SetOperations<String,Long> setOperations= redisTemplate.opsForSet();
        Long numMembers=setOperations.size(String.valueOf(groupId));
        if(group.getIsProceeding()){
            return 1L;
        }
        else{
            if(numMembers!=0L){
                return -1L;
            }
            else{
                return -2L;
            }
        }
    }
    public Boolean isMember(Long groupId, Long userId){
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        return groupId.equals(hashOperations.get("userBelong",userId));
    }
}
