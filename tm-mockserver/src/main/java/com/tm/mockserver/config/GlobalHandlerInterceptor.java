package com.tm.mockserver.config;

import com.tm.mockserver.service.CustomizeMockRequestHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class GlobalHandlerInterceptor implements HandlerInterceptor {
    public static final String SESSION_KEY_USER = "username";
    public static final String CUSTOMIZE_MOCK_URI_PREFIX = "/__customize_mock/";

    @Autowired
    private CustomizeMockRequestHandleService customizeMockRequestHandleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if(requestURI.startsWith(CUSTOMIZE_MOCK_URI_PREFIX)) {
            customizeMockRequestHandleService.handle(request, response);
            return false;
        }
        if(requestURI.startsWith("/mock")) {
            return true;
        }
        return false;
    }
}
