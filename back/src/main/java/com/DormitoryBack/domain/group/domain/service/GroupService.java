package com.DormitoryBack.domain.group.domain.service;

import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.group.domain.dto.request.GroupCreateDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupCreatedDto;
import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RedisTemplate<String,Long> redisTemplate;

    public GroupCreatedDto createNewGroup(GroupCreateDto requestDto) {
        Article article=requestDto.getArticle();

        Group newGroup=Group.builder()
                .dormId(article.getDorId())
                .hostId(article.getUserId())
                .article(article)
                .createdTime(LocalDateTime.now())
                .category(article.getCategory())
                .membersId(new HashSet<>())
                .isProceeding(true)
                .build();

        Group saved=groupRepository.save(newGroup);
        GroupCreatedDto responseDto=GroupCreatedDto.builder()
                .id(saved.getId())
                .dormId(saved.getDormId())
                .hostId(saved.getHostId())
                .category(saved.getCategory())
                .build();

        initRedisSet(newGroup);
        return responseDto;
    }
    public void initRedisSet(Group newGroup){
        //redis에 {groupId:membersId:(hostId,)} 형태로 초기값이 세팅됨
        SetOperations<String,Long> setOfMembersId=redisTemplate.opsForSet();
        setOfMembersId.add(newGroup.getId().toString(), newGroup.getHostId());
    }

    public List<GroupCreatedDto> getAllProceedingGroups() {
        List<Group> proceedingGroups=groupRepository
                .findAllByIsProceeding(true);

        List<GroupCreatedDto> responseDto=proceedingGroups
                .stream()
                .map(Group::groupToCreatedDto)
                .collect(Collectors.toList());

        return responseDto;

    }

}
