package com.DormitoryBack.domain.report.api;

import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.report.dto.ReportDTO;
import com.DormitoryBack.domain.report.entity.Report;
import com.DormitoryBack.domain.report.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public ResponseEntity allReports(){
        List<Report> reports=reportService.getAllReports();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportService.listStringify(reports));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity Report(@PathVariable("reportId") Long reportId){
        Report report=reportService.getReport(reportId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(report.toJsonString());
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity articleReports(@PathVariable("articleId") Long articleId){
        List<Report> reports=reportService.getArticleReports(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportService.listStringify(reports));
    }
    @GetMapping("/comment/{commentId}")
    public ResponseEntity commentReports(@PathVariable("commentId") Long commentId){
        List<Report> reports=reportService.getCommentReports(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportService.listStringify(reports));
    }
    @GetMapping("user/{userId}")
    public ResponseEntity userReports(@PathVariable("userId") Long userId){
        List<Report> reports=reportService.getUserReports(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportService.listStringify(reports));
    }
    @PostMapping("new")
    public ResponseEntity newReport(@RequestBody ReportDTO dto, @RequestHeader("Authorization") String token){
        Report report=reportService.newReport(dto,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(report.toJsonString());
    }

    //신고를 처리하는 마이크로 서비스를 따로 만들기(ML,NLP,Python)
    //ML을 위한 학습 데이터를 어디서 구하지? <- 인터넷에서 긁어온다고 하더라도
    //현재 맥락에 100% 적합하다고 볼 수는 없음.




}
