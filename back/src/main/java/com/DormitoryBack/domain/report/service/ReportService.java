package com.DormitoryBack.domain.report.service;

import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.report.dto.InquireDTO;
import com.DormitoryBack.domain.report.dto.ReportDTO;
import com.DormitoryBack.domain.report.entity.Report;
import com.DormitoryBack.domain.report.repository.ReportRepository;
import com.DormitoryBack.module.TimeOptimizer;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;


    public List<Report> getAllReports() {
        List<Report> reports=reportRepository.findAll();
        if(reports.isEmpty()){
            throw new RuntimeException("NoReportFound");
        }
        return reports;
    }

    public List<String> listStringify(List<Report> reportList){
        List<String> stringifiedReportList=reportList.stream()
                .map(Report::toJsonString)
                .collect(Collectors.toList());

        return stringifiedReportList;
    }

    public Report getReport(Long reportId) {
        Report report=reportRepository.findById(reportId).orElse(null);
        if(report==null){
            throw new RuntimeException("NoFound");
        }
        return report;
    }


    public List<Report> getUserReports(Long userId){
        User user=userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        List<Report> reports=reportRepository.findAllByReporter(user);
        if(reports.isEmpty()){
            throw new RuntimeException("NoReportFound");
        }
        return reports;
    }

    @Transactional
    public Report newReport(ReportDTO dto, String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new JwtException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User user=userRepository.findById(userId).orElse(null);
        if(user==null) {
            throw new RuntimeException("UserNotFound");
        }
        Report newReport=Report.builder()
                .reporter(user)
                .targetId(dto.getTargetId())
                .reportType(dto.getReportType())
                //.time(LocalDateTime.now())
                .time(TimeOptimizer.now())
                .reason(dto.getReason())
                .build();

        Report saved=reportRepository.save(newReport);
        return saved;
    }

    @Transactional
    public Report newInquire(InquireDTO dto, String token){
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User user=userRepository.findById(userId).orElse(null);
        Report newInquire=Report.builder()
            .reporter(user)
            .targetId(null)
            .reportType(null)
            .time(TimeOptimizer.now())
            .reason("INQUIRE") //현재는 null로 지정
            .inquireContent(dto.getInquireContent())
            .build();

        Report saved=reportRepository.save(newInquire);
        return saved;    
    }

    @Transactional
    public void deleteReport(Long reportId){
        Report target=reportRepository.findById(reportId).orElse(null);
        if(target==null){
            throw new IllegalArgumentException("ReportNotFound");
        }
        reportRepository.delete(target);
    }

}
