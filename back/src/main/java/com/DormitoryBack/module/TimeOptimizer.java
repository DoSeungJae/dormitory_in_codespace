package com.DormitoryBack.module;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeOptimizer {
    
    public static LocalDateTime now() {

        ZonedDateTime now=ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return now.toLocalDateTime();

    }
}
