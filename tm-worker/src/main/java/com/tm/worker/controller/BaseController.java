package com.tm.worker.controller;

import com.tm.common.base.model.User;
import com.tm.worker.ehcache.EhcacheService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


public class BaseController {
    @Autowired
    private EhcacheService ehcacheService;

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public User getLoginUser() {
        HttpServletRequest request = getRequest();
        if(request.getUserPrincipal() == null) {
            return new User();
        }
        String username = request.getUserPrincipal().getName();
        return ehcacheService.get(username);
    }
}
