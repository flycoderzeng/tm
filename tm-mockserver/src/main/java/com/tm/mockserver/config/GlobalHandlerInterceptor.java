package com.tm.mockserver.config;

import com.tm.common.base.model.User;
import com.tm.mockserver.ehcache.EhcacheService;
import com.tm.mockserver.service.CustomizeMockRequestHandleService;
import com.tm.mockserver.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class GlobalHandlerInterceptor implements HandlerInterceptor {
    public static final String AUTHORIZE_TOKEN = "token";
    public static final String SESSION_KEY_USER = "username";
    public static final String CUSTOMIZE_MOCK_URI_PREFIX = "/__customize_mock/";

    @Autowired
    private EhcacheService ehcacheService;

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
        String token = request.getHeader(AUTHORIZE_TOKEN);

        if(StringUtils.isEmpty(token)) {
            log.error("header 里面没有 " + AUTHORIZE_TOKEN);
            return false;
        }
        try {
            Claims claims = JwtUtils.parseJWT(token);
            String username = claims.getSubject();
            User user = ehcacheService.get(username);
            if(user == null) {
                log.error("加载用户信息失败，可能是数据库用户表里面没有该用户");
                return false;
            }
            request.getSession().setAttribute(SESSION_KEY_USER, username);
            log.info("username is {}, path: {}", user.getUsername(), requestURI);
        } catch (Exception e) {
            log.error("解析jwt异常: ", e);
            return false;
        }
        return true;
    }
}
