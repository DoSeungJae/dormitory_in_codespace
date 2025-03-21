package com.DormitoryBack.config;

import com.DormitoryBack.domain.jwt.TokenProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token-validity-in-seconds}")
    private Long tokenValidityMilliseconds;

    @Bean
    public TokenProvider tokenProvider(){

        return new TokenProvider(secret,tokenValidityMilliseconds);
    }
}
