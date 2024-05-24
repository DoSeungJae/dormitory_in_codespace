package com.DormitoryBack.domain.group.domain.service;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.group.domain.dto.request.GroupCreateDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupChangedDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupCreatedDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupListDto;
import com.DormitoryBack.domain.group.domain.dto.response.SingleGroupDto;
import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.member.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private final RedisTemplate<String,Long> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public TokenProvider tokenProvider;

    public GroupService(RedisTemplate<String,Long> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    @Transactional
    public GroupCreatedDto createNewGroup(@NotNull GroupCreateDto requestDto) {
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();

        Long articleId=requestDto.getArticleId();
        if(requestDto.getMaxCapacity()==null){
            requestDto.setMaxCapacity(4L);
        }
        else if(requestDto.getMaxCapacity()<2L ){
            throw new RuntimeException("MaxCapacityMustNotLessThan2");
        }
        else if(requestDto.getMaxCapacity()>10L){
            throw new RuntimeException("MaxCapacityCannotExceed10");
        }
        Article article=articleRepository.findById(articleId).orElse(null);
        if(article==null){
            throw new RuntimeException("ArticleNotFound");
        }
        Long hostId=article.getUserId();
        Long NotBelongToAnywhere=setOperations.add("groupGlobal",hostId);
        if(NotBelongToAnywhere==0L){
            throw new RuntimeException("DuplicatedParticipation");
        }

        Group newGroup=Group.builder()
                .id(article.getId())
                .dormId(article.getDorId())
                .hostId(hostId)
                .maxCapacity(requestDto.getMaxCapacity())
                .createdTime(LocalDateTime.now())
                .category(article.getCategory())
                .isProceeding(true)
                .build();

        Group saved=groupRepository.save(newGroup);
        GroupCreatedDto responseDto=GroupCreatedDto.builder()
                .id(saved.getId())
                .dormId(saved.getDormId())
                .hostId(saved.getHostId())
                .category(saved.getCategory())
                .maxCapacity(saved.getMaxCapacity())
                .createdTime(saved.getCreatedTime())
                .isProceeding(saved.getIsProceeding())
                .currentNumberOfMembers(1L)
                .build();

        setOperations.add(String.valueOf(saved.getId()),saved.getHostId());
        hashOperations.put("userBelong",saved.getHostId(),saved.getId());
        return responseDto;
    }

    public GroupListDto getAllGroups() {
        List<Group> groups=groupRepository.findAll();

        List<GroupCreatedDto> createdDtoList=this
                .groupListToCreatedDtoList(groups);
        Long groupCnt=Long.valueOf(groups.size());
        List<String> stringified=stringifyDtoList(createdDtoList);
        GroupListDto responseDto=GroupListDto.builder()
                .groups(stringified)
                .numberOfGroup(groupCnt)
                .build();

        return responseDto;
    }
    public SingleGroupDto getGroupThatUserBelongsTo(String token) {
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        Long groupId=hashOperations.get("userBelong",userId);
        if(groupId==null){
            throw new RuntimeException("UserIndependent");
        }
        SingleGroupDto responseDto=this.getGroup(groupId);
        return responseDto;
    }
    public SingleGroupDto getGroup(Long groupId){
        Group group=groupRepository.findById(groupId).orElse(null);
        if(group==null){
            throw new RuntimeException("GroupNotFound");
        }
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        Long memberCnt=setOperations.size(String.valueOf(groupId));
        List<Long> membersId= new ArrayList<>(setOperations.members(String.valueOf(groupId)));
        Iterator<Long> iterator=membersId.iterator();
        List<UserResponseDTO> userDtoList=new ArrayList<>();
        while(iterator.hasNext()){
            User member=userRepository.findById(iterator.next()).orElse(null);
            UserResponseDTO userDto=UserResponseDTO.builder()
                    .eMail(member.getEMail())
                    .nickName(member.getNickName())
                    .id(member.getId())
                    .build();

            userDtoList.add(userDto);
        }
        List<String> stringified=stringifyUserDtoList(userDtoList);
        SingleGroupDto responseDto=SingleGroupDto.builder()
                .id(group.getId())
                .dormId(group.getDormId())
                .hostId(group.getHostId())
                .createdTime(group.getCreatedTime())
                .category(group.getCategory())
                .maxCapacity(group.getMaxCapacity())
                .isProceeding(group.getIsProceeding())
                .members(stringified)
                .currentNumberOfMembers(memberCnt)
                .build();

        return responseDto;
    }

    public GroupListDto getAllProceedingGroups() {
        List<Group> groups=groupRepository.findAllByIsProceeding(true);
        Long groupCnt=Long.valueOf(groups.size());
        List<GroupCreatedDto> createdDtoList=groupListToCreatedDtoList(groups);
        List<String> stringified=stringifyDtoList(createdDtoList);
        GroupListDto responseDto=GroupListDto.builder()
                .groups(stringified)
                .numberOfGroup(groupCnt)
                .build();

        return responseDto;
    }

    public long getNumberOfMembers(Long groupId) {
        if(groupId==-1L){
            throw new RuntimeException("GroupIdNotGiven");
        }
        long num=redisTemplate.opsForSet().size(groupId.toString());
        return num;
    }

    public GroupChangedDto participateInGroup(Long groupId, String token) {
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        if(groupId==-1L){
            throw new RuntimeException("GroupIdNotGiven");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        Long numBeforeAdding=setOperations.size(String.valueOf(groupId));
        Group targetGroup=groupRepository.findById(groupId).orElse(null);
        Boolean isMemberOfTargetGroup=setOperations.isMember(String.valueOf(targetGroup.getId()),userId);
        if(isMemberOfTargetGroup==true){
            throw new RuntimeException("AlreadyBelongToThisGroup");
        }
        Long notBelongToAnywhere=setOperations.add("groupGlobal",userId);
        if(notBelongToAnywhere==0L){
            throw new RuntimeException("DuplicatedParticipation");
        }
        if(numBeforeAdding==targetGroup.getMaxCapacity()){
            throw new RuntimeException("GroupFull");
        }
        setOperations.add(String.valueOf(targetGroup.getId()),userId);
        hashOperations.put("userBelong",userId,targetGroup.getId());

        User user=userRepository.findById(userId).orElse(null);
        UserResponseDTO userChanges= UserResponseDTO.builder()
                .eMail(user.getEMail())
                .nickName(user.getNickName())
                .id(user.getId())
                .build();

        GroupChangedDto responseDto=GroupChangedDto.builder()
                .mode("join")
                .userChanges(userChanges)
                .numberOfRemainings(numBeforeAdding+1L)
                .build();

        return responseDto;
    }

    public GroupChangedDto leaveGroup(Long groupId, String token){
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        if(groupId==-1L){
            throw new RuntimeException("GroupIdNotGiven");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        Long numBeforeOps=setOperations.size(String.valueOf(groupId));
        Group targetGroup=groupRepository.findById(groupId).orElse(null);
        Boolean isMemberOfTargetGroup=setOperations.isMember(String.valueOf(groupId),userId);

        if(isMemberOfTargetGroup==false){
            throw new RuntimeException("UserNotBelongToGroupToLeave");
        }
        else if(userId==targetGroup.getHostId()){
            throw new RuntimeException("HostCannotLeaveGroup");
        }
        setOperations.remove(String.valueOf(groupId),userId);
        setOperations.remove("groupGlobal",userId);
        hashOperations.delete("userBelong",userId);
        User user=userRepository.findById(userId).orElse(null);
        UserResponseDTO userChanges=UserResponseDTO.builder()
                .eMail(user.getEMail())
                .nickName(user.getNickName())
                .id(userId)
                .build();

        GroupChangedDto responseDto=GroupChangedDto.builder()
                .mode("quit")
                .userChanges(userChanges)
                .numberOfRemainings(numBeforeOps-1L)
                .build();

        return responseDto;
    }

    public GroupChangedDto expelUser(Long groupId, String hostToken, Long targetUserId){
        if(!tokenProvider.validateToken(hostToken)){
            throw new JwtException("InvalidToken");
        }
        if(groupId==-1L){
            throw new RuntimeException("GroupIdNotGiven");
        }
        Long userId=tokenProvider.getUserIdFromToken(hostToken);
        Group group=groupRepository.findById(groupId).orElse(null);
        if(group==null){
            throw new RuntimeException("GroupNotFound");
        }
        if(group.getHostId()!=userId){
            throw new RuntimeException("NoPermission");
        }
        if(userId==targetUserId){
            throw new RuntimeException("CannotExpelOneself");
        }
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        Long numBeforeOps=setOperations.size(String.valueOf(groupId));
        Boolean isMemberOfTheGroup=setOperations.isMember(String.valueOf(groupId),targetUserId);
        if(isMemberOfTheGroup==false){
            throw new RuntimeException("UserNotBelongToGroupToBeExpelled");
        }
        setOperations.remove(String.valueOf(groupId),targetUserId);
        setOperations.remove("groupGlobal",targetUserId);
        hashOperations.delete("userBelong",targetUserId);
        User expelledUser=userRepository.findById(targetUserId).orElse(null);
        UserResponseDTO userChanges=UserResponseDTO.builder()
                .eMail(expelledUser.getEMail())
                .nickName(expelledUser.getNickName())
                .id(targetUserId)
                .build();

        GroupChangedDto responseDto=GroupChangedDto.builder()
                .mode("expel")
                .userChanges(userChanges)
                .numberOfRemainings(numBeforeOps-1L)
                .build();

        return responseDto;

    }

    @Transactional
    public void finishGroup(Long groupId, String token, Long force) {
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Group group=groupRepository.findById(groupId).orElse(null);
        if(group==null){
            throw new RuntimeException("GroupNotFound");
        }
        if(group.getHostId()!= tokenProvider.getUserIdFromToken(token)){
            throw new RuntimeException("NoPermission");
        }
        if(group.getIsProceeding()==false && setOperations.size(String.valueOf(groupId))==0){
            throw new RuntimeException("AlreadyFinishedGroup");
        }
        if(setOperations.size(String.valueOf(groupId))>=2L && force==0L){
            throw new RuntimeException("CannotFinishWhileRecruiting");
        }
        Set<Long> membersId=(setOperations.members(String.valueOf(groupId)));
        Iterator<Long> iterator=membersId.iterator();
        while(iterator.hasNext()){
            Long memberId=iterator.next();
            setOperations.remove(String.valueOf(groupId),memberId);
            setOperations.remove("groupGlobal",memberId);
            hashOperations.delete("userBelong",memberId);
        }
        group.close();
        Group saved=groupRepository.save(group);

        return ;
    }

    @Transactional
    public void closeGroup(Long groupId, String token) {
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Group group=groupRepository.findById(groupId).orElse(null);
        if(group==null){
            throw new RuntimeException("GroupNotFound");
        }
        if(group.getHostId()!= tokenProvider.getUserIdFromToken(token)){
            throw new RuntimeException("NoPermission");
        }
        if(group.getIsProceeding()==false){
            throw new RuntimeException("AlreadyClosedGroup");
        }
        group.close();
        Group saved=groupRepository.save(group);

        return ;
    }

    public Boolean getIsMember(Long groupId, String token) {
        HashOperations<String,Long,Long> hashOperations=redisTemplate.opsForHash();
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Long memberId= tokenProvider.getUserIdFromToken(token);
        //둘다 타입은 Long이지만 ==로 판단하면 false가 나옴
        //단, memberId가 host인 경우에 true가 나옴
        //자세하게 파봐야됨
        if(groupId.equals(hashOperations.get("userBelong",memberId))){
            return true;
        }
        else{
            return false;
        }
    }

    public List<String> stringifyDtoList(@NotNull List<GroupCreatedDto> dtoList){
        List<String> stringifiedDtoList=dtoList.stream()
                .map(GroupCreatedDto::toJsonString)
                .collect(Collectors.toList());

        return stringifiedDtoList;
    }

    public List<String> stringifyUserDtoList(@NotNull List<UserResponseDTO> dtoList){
        List<String> stringifiedDtoList=dtoList.stream()
                .map(UserResponseDTO::toJsonString)
                .collect(Collectors.toList());

        return stringifiedDtoList;
    }
    public List<GroupCreatedDto> groupListToCreatedDtoList(@NotNull List<Group> groups){
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        List<GroupCreatedDto> createdDtoList=new ArrayList<>();
        Iterator<Group> iterator=groups.iterator();

        while(iterator.hasNext()){
            Group group=iterator.next();
            Long numMembers=setOperations.size(String.valueOf(group.getId()));
            GroupCreatedDto responseDto=GroupCreatedDto.builder()
                    .id(group.getId())
                    .dormId(group.getDormId())
                    .hostId(group.getHostId())
                    .category(group.getCategory())
                    .maxCapacity(group.getMaxCapacity())
                    .isProceeding(group.getIsProceeding())
                    .createdTime(group.getCreatedTime())
                    .currentNumberOfMembers(numMembers)
                    .build();

            createdDtoList.add(responseDto);
        }
        return createdDtoList;
    }



}
