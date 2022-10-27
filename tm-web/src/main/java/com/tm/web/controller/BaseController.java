package com.tm.web.controller;

import com.tm.common.base.model.User;
import com.tm.web.ehcache.EhcacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


public class BaseController {
    @Autowired
    private EhcacheService ehcacheService;

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public User getLoginUser() {
        HttpServletRequest request = getRequest();
        String username = request.getUserPrincipal().getName();
        User user = ehcacheService.get(username);
        return user;
    }
}
