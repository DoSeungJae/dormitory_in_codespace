package com.DormitoryBack.domain.group.domain.api;


import com.DormitoryBack.domain.group.domain.dto.request.GroupCreateDto;

import com.DormitoryBack.domain.group.domain.dto.response.GroupCreatedDto;
import com.DormitoryBack.domain.group.domain.service.GroupService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public GroupApi(GroupService groupService){
        this.groupService=groupService;
    }
    @GetMapping("/test")
    public String groupTest(){
        return "test in group domain";
    }

    @GetMapping("")
    public ResponseEntity allProceedingGroups(){
        List<GroupCreatedDto> responseDto=groupService.getAllProceedingGroups();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/new")
    public ResponseEntity newGroup(@RequestBody GroupCreateDto requestDto){
        GroupCreatedDto responseDto=groupService.createNewGroup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PatchMapping("/join/{userId}")
    public ResponseEntity newJoiner(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @PatchMapping("quit/{userId}")
    public ResponseEntity quit(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
     @DeleteMapping("/delete/{groupId}")
    public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
