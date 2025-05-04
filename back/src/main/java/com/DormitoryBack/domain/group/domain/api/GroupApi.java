package com.DormitoryBack.domain.group.domain.api;


import com.DormitoryBack.domain.group.domain.dto.request.GroupCreateDto;

import com.DormitoryBack.domain.group.domain.dto.response.GroupChangedDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupCreatedDto;
import com.DormitoryBack.domain.group.domain.dto.response.GroupListDto;
import com.DormitoryBack.domain.group.domain.dto.response.SingleGroupDto;
import com.DormitoryBack.domain.group.domain.service.GroupService;
import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
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
    public ResponseEntity<GroupListDto> allGroups(){
        GroupListDto responseDto=groupService.getAllGroups();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/proceedings")
    public ResponseEntity<GroupListDto> allProceedingGroups(){
        GroupListDto responseDto=groupService.getAllProceedingGroups();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @GetMapping("/userBelongsTo")
    public ResponseEntity<SingleGroupDto> groupThatUserBelongsTo(@RequestHeader("Authorization") String token){
        SingleGroupDto responseDto=groupService.getGroupThatUserBelongsTo(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<SingleGroupDto> group(@PathVariable("groupId") Long groupId){
        SingleGroupDto responseDto=groupService.getGroup(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @GetMapping("/host/{groupId}")
    public ResponseEntity<UserResponseDTO> groupHost(@PathVariable("groupId") Long groupId){
        UserResponseDTO responseDto=groupService.getGroupHost(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/numMembers")
    public ResponseEntity<Long> numberOfMembers(
            @RequestParam(name="groupId",defaultValue = "-1") Long groupId){
        long num=groupService.getNumberOfMembers(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(num);
    }
    @GetMapping("/isMember")
    public ResponseEntity<Boolean> isMember(@RequestParam(name = "groupId", defaultValue = "-1") Long groupId,
                                   @RequestHeader("Authorization") String token){

        Boolean isMember=groupService.getIsMember(groupId,token);
        return ResponseEntity.status(HttpStatus.OK).body(isMember);

    }

    @GetMapping("/stateFromExternal/{groupId}")
    public ResponseEntity<Long> groupStateFromExternalView(@PathVariable("groupId") Long groupId,
                                                     @RequestHeader("Authorization") String token){

        Long groupState=groupService.getGroupStateFromExternalView(groupId,token);
        return ResponseEntity.status(HttpStatus.OK).body(groupState);
    }

    @GetMapping("/isGroupProceeding/{groupId}")
    public ResponseEntity<Boolean> checkProceeding(@PathVariable("groupId") Long groupId){
        Boolean isGroupProceeding=groupService.checkGroupProceeding(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(isGroupProceeding);
    }

    @GetMapping("/groupMaxCapacity/{groupId}")
    public ResponseEntity<Long> groupMaxCapacity(@PathVariable("groupId") Long groupId){
        Long maxCapacity=groupService.getMaxCapacity(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(maxCapacity);
    }

    @PostMapping("/new")
    public ResponseEntity<GroupCreatedDto> newGroup(@RequestBody GroupCreateDto requestDto){
        GroupCreatedDto responseDto=groupService.createNewGroup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PatchMapping("/participate")
    public ResponseEntity<GroupChangedDto> newJoiner(
            @RequestHeader("Authorization") String token,
            @RequestParam(name="groupId", defaultValue = "-1") Long groupId){

        GroupChangedDto responseDto = groupService
                .participateInGroup(groupId, token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("quit")
    public ResponseEntity<GroupChangedDto> quit(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "groupId",defaultValue = "-1") Long groupId){

        GroupChangedDto responseDto=groupService
                .leaveGroup(groupId,token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PatchMapping("expel")
    public ResponseEntity<GroupChangedDto> invite(
            @RequestHeader("Authorization") String hostToken,
            @RequestParam(name = "groupId", defaultValue = "-1") Long groupId,
            @RequestParam(name="targetId", defaultValue = "-1") Long targetId){

        GroupChangedDto responseDto=groupService
                .expelUser(groupId,hostToken,targetId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PatchMapping("/finish/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable("groupId") Long groupId,
                                      @RequestHeader("Authorization") String token,
                                      @RequestParam(name="force",defaultValue = "0") Long force){

        groupService.finishGroup(groupId,token,force);
        return ResponseEntity.status(HttpStatus.OK).body("GroupFinished");
    }
    @PatchMapping("/close/{groupId}")
    public ResponseEntity<String> closeGroup(@PathVariable("groupId") Long groupId,
                                     @RequestHeader("Authorization") String token){

        groupService.closeGroup(groupId,token);
        return ResponseEntity.status(HttpStatus.OK).body("GroupClosed");
    }


}
