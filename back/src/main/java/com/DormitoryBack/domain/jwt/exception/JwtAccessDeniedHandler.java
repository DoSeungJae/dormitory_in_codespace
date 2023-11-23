package com.DormitoryBack.domain.jwt.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

//필요한 권한이 존재하지 않는 경우에 403 Forbidden 에러를 리터하기 위한 클래스
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException{
        response.sendError(HttpServletResponse.SC_FORBIDDEN); //403
    }
}
