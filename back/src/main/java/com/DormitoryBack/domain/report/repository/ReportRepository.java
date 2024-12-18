package com.DormitoryBack.domain.report.repository;

import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {

    List<Report> findAllByReporter(User reporter);

}
