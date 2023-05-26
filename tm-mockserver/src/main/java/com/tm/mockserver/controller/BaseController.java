package com.tm.mockserver.controller;

import com.tm.common.base.model.User;
import com.tm.mockserver.config.GlobalHandlerInterceptor;
import com.tm.mockserver.ehcache.EhcacheService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class BaseController {
    @Autowired
    private EhcacheService ehcacheService;

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public User getLoginUser() {
        HttpServletRequest request = getRequest();
        String username = request.getSession().getAttribute(GlobalHandlerInterceptor.SESSION_KEY_USER).toString();
        User user = ehcacheService.get(username);
        return user;
    }
}
