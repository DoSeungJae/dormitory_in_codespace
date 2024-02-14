package com.DormitoryBack.domain.report.repository;

import com.DormitoryBack.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
