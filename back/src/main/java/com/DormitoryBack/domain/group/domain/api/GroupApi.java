package com.DormitoryBack.domain.group.domain.api;


import com.DormitoryBack.domain.group.domain.dto.request.GroupCreateDto;

import com.DormitoryBack.domain.group.domain.dto.response.GroupChangedDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupCreatedDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupListDto;
import com.DormitoryBack.domain.group.domain.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/group")
@Slf4j
public class GroupApi {
    private final GroupService groupService;

    @Autowired
    public GroupApi(GroupService groupService){
        this.groupService=groupService;
    }
    @GetMapping("/test")
    public String groupTest(){
        return "test in group domain";
    }


    @GetMapping("")
    public ResponseEntity allGroups(){
        GroupListDto responseDto=groupService.getAllGroups();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @GetMapping("/proceedings")
    public ResponseEntity allProceedingGroups(){
        List<GroupCreatedDto> responseDto=groupService.getAllProceedingGroups();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/numMembers")
    public ResponseEntity numberOfMembers(
            @RequestParam(name="groupId",defaultValue = "-1") Long groupId){

        long num=groupService.getNumberOfMembers(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(num);
    }

    @PostMapping("/new")
    public ResponseEntity newGroup(@RequestBody GroupCreateDto requestDto){
        GroupCreatedDto responseDto=groupService.createNewGroup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PatchMapping("/join")
    public ResponseEntity newJoiner(
            @RequestHeader("Authorization") String token,
            @RequestParam(name="groupId", defaultValue = "-1") Long groupId){

        GroupChangedDto responseDto = groupService
                .participateInGroup(groupId, token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("quit")
    public ResponseEntity quit(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "groupId",defaultValue = "-1")
            Long groupId){

        GroupChangedDto responseDto=groupService
                .leaveGroup(groupId,token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
