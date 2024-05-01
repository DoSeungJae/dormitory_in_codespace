package com.DormitoryBack.domain.group.domain.api;


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

    @GetMapping("/test")
    public String groupTest(){
        return "test in group domain";
    }

    @GetMapping("")
    public ResponseEntity allGroups(){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/new")
    public ResponseEntity newGroup(){
        return ResponseEntity.status(HttpStatus.OK).body(null);
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
