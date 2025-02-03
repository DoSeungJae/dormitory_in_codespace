package com.DormitoryBack.domain.member.restriction.domain.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name="restriction")
@AllArgsConstructor
@NoArgsConstructor
public class Restriction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false, name = "triggered_time")
    private LocalDateTime triggeredTime;

    @Column(nullable = true, name="duration_days")
    private Long durationDays; //이 값이 null이면 "경고"로 간주.

}
