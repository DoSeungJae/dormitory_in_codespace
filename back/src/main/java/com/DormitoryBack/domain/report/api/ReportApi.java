package com.DormitoryBack.domain.report.api;

import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.report.entity.Report;
import com.DormitoryBack.domain.report.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("api/v1/report")
@Slf4j
public class ReportApi {
    private final ReportService reportService;
    private final TokenProvider tokenProvider;

    @Autowired
    public ReportApi(ReportService reportService,TokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
        this.reportService=reportService;
    }

    @GetMapping("/test")
    public ResponseEntity reportTest(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("ReportTest");
    }




}
