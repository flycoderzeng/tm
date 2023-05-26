package com.tm.common.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class SecurityInterceptor implements HandlerInterceptor {

    public static final String HTTP_LOCALHOST_3000 = "http://localhost:3000";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {
        // 此处配置的是允许任意域名跨域请求，可根据需求指定
        response.setHeader("Access-Control-Allow-Origin", HTTP_LOCALHOST_3000);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        //这个我是根据项目来定的
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Range, X-E4M-With, X-Request-With, Accept");

        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }
        return true;
    }
}
