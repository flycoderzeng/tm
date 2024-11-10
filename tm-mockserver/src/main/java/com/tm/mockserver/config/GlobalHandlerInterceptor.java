package com.tm.mockserver.config;

import com.tm.mockserver.service.CustomizeMockRequestHandleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@Component
public class GlobalHandlerInterceptor implements HandlerInterceptor {
    public static final String SESSION_KEY_USER = "username";
    public static final String CUSTOMIZE_MOCK_URI_PREFIX = "/__customize_mock/";

    private final CustomizeMockRequestHandleService customizeMockRequestHandleService;

    public GlobalHandlerInterceptor(CustomizeMockRequestHandleService customizeMockRequestHandleService) {
        this.customizeMockRequestHandleService = customizeMockRequestHandleService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if(requestURI.startsWith(CUSTOMIZE_MOCK_URI_PREFIX)) {
            customizeMockRequestHandleService.handle(request, response);
            return false;
        }
        return requestURI.startsWith("/mock");
    }
}
