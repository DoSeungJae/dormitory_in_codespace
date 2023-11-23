package com.DormitoryBack.config;

import com.DormitoryBack.domain.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TokenProvider tokenProvider(){

        return new TokenProvider();
    }
}
