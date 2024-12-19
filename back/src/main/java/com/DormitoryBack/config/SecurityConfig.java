package com.DormitoryBack.config;
import com.DormitoryBack.domain.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    //private final TokenProvider tokenProvider1;
    //private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint1;
    //private final JwtAccessDeniedHandler jwtAccessDeniedHandle1r;
    //@Autowired
    //private final UserDetailService userDetailService1;

    //private final AuthenticationManagerBuilder authenticationManagerBuilder1;


    public SecurityConfig(
            TokenProvider tokenProvider,
            //JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint1,
            //JwtAccessDeniedHandler jwtAccessDeniedHandler1,
            //UserDetailService userDetailService1,
            AuthenticationManagerBuilder authenticationManagerBuilder){

        //this.tokenProvider=tokenProvider;
        //this.jwtAuthenticationEntryPoint=jwtAuthenticationEntryPoint;
        //this.jwtAccessDeniedHandler=jwtAccessDeniedHandler;
        //this.userDetailService=userDetailService;
        //this.authenticationManagerBuilder=authenticationManagerBuilder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                //.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //.accessDeniedHandler(jwtAccessDeniedHandler)

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
                .anyRequest()
                .authenticated();

                //.and()
                //.apply(new JwtSecurityConfig(tokenProvider))

                //.and()
                //.authenticationProvider(daoAuthenticationProvider()); //??

        return http.build();
    }



    /*
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider =  new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // ?
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        return daoAuthenticationProvider;
    }

     */



}

