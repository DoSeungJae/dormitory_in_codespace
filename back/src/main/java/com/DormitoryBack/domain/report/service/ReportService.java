package com.DormitoryBack.domain.report.service;

import com.DormitoryBack.domain.report.entity.Report;
import com.DormitoryBack.domain.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

}
