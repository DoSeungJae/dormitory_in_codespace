package com.DormitoryBack.domain.report.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long articleId;
    private Long commentId;
    private LocalDateTime time;
    private String reason;

}
