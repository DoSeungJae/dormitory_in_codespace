package com.DormitoryBack.domain.report.dto;
import com.DormitoryBack.domain.report.enums.ReportType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    private Long targetId;
    private String reason;

}
