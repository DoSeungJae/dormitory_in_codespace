package com.DormitoryBack.domain.member.restriction.domain.entity;

import java.util.List;

import com.DormitoryBack.domain.member.restriction.domain.enums.Functions;
import com.DormitoryBack.domain.notification.enums.EntityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@Entity
@Table(name="restriction")
public class Restriction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private Long suspensionDays;

    @Column(nullable = false)
    private int suspendedFunctions;

    public boolean isSuspended(Functions function){
        return (suspendedFunctions & function.getValue()) != 0;
    }

    public void suspendFunction(Functions functions){
        suspendedFunctions |= functions.getValue();
    }

    public void releaseFunction(Functions functions){
        suspendedFunctions &= ~functions.getValue();
    }


    /* 사용 예시
    Restriction restriction = new Restriction();
        restriction.addRestriction(Restriction.USER);
    restriction.addRestriction(Restriction.ARTICLE);

    if (restriction.isRestricted(Functions.USER)) {
        System.out.println("USER 기능이 제한되었습니다.");
    }

    if (!restriction.isRestricted(Functions.GROUP)) {
        System.out.println("GROUP 기능은 제한되지 않았습니다.");
    }

    */
    
}
