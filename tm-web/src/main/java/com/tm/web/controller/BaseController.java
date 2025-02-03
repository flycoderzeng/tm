package com.tm.web.controller;

import com.tm.common.base.model.User;
import com.tm.web.ehcache.EhcacheService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



public class BaseController {
    @Autowired
    private EhcacheService ehcacheService;
    public static String OS = System.getProperty("os.name").toLowerCase();

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
