package com.DormitoryBack.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf().disable()
                .exceptionHandling()

                //h2-console 사용
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                //세션을 사용하지 않으므로 stateless로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .requestMatchers("api/v1/user/**")
                .permitAll()
                .requestMatchers("api/v1/article/**")
                .permitAll()
                .requestMatchers("api/v1/token/**")
                .permitAll()
                .requestMatchers("api/v1/report/**")
                .permitAll()
                .requestMatchers("api/v1/comment/**")
                .permitAll()
                .requestMatchers("api/v1/group/**")
                .permitAll()
                .requestMatchers("api/v1/chat/**")
                .permitAll()
                .requestMatchers("api/v1/notification/**")
                .permitAll()
                .requestMatchers("api/v1/restriction/**")
                .permitAll()
                .requestMatchers("api/v1/oauth/**")
                .permitAll()
                .requestMatchers("api/v1/email/**")
                .permitAll()
                .requestMatchers("api/v1/file/**")
                .permitAll()
                .requestMatchers("api/v1/block/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        return http.build();
    }

}

