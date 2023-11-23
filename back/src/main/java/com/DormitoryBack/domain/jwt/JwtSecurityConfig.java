package com.DormitoryBack.domain.jwt;



/*
//TokenProvider,JwtFilter를 SecurityConfig에 적용할 때 사용할 클래스
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http){
        JwtFilter customFilter= new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

 */
