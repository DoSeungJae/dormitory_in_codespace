package com.DormitoryBack.domain.member.restriction.domain.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionRequestDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTOList;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionService;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("/api/v1/restriction")
@Slf4j
public class RestrictionAPI {

    private final RestrictionService restrictionService;

    @Autowired
    public RestrictionAPI(RestrictionService restrictionService){
        this.restrictionService=restrictionService;
    }

    @GetMapping("/test")
    public String restrictionTest(){
        return "test in restriction domain";
    }

    @GetMapping("/my")
    public ResponseEntity<RestrictionResponseDTOList> myRestrictions(@RequestHeader("Authorization") String token){
        RestrictionResponseDTOList dtoList=restrictionService.getMyRestrictions(token);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PostMapping("/new")
    public ResponseEntity<RestrictionResponseDTO> makeRestriction(@RequestBody RestrictionRequestDTO DTO){
        RestrictionResponseDTO responseDTO=restrictionService.restrict(DTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }



    
}
