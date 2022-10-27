package com.tm.worker.config;

import com.tm.common.base.model.User;
import com.tm.worker.ehcache.EhcacheService;
import com.tm.worker.utils.JwtUtils;
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


    @Autowired
    private EhcacheService ehcacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().equals("/login")) {
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
            log.info("username is {}, path: {}", user.getUsername(), request.getRequestURI());
        } catch (Exception e) {
            log.error("解析jwt异常: {}", e);
            return false;
        }
        return true;
    }
}
