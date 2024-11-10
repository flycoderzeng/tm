package com.tm.mockserver.controller;

import com.tm.common.base.model.User;
import com.tm.mockserver.config.GlobalHandlerInterceptor;
import com.tm.mockserver.ehcache.EhcacheService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@RestController
public class BaseController {
    private final EhcacheService ehcacheService;

    @Inject
    public BaseController(EhcacheService ehcacheService) {
        this.ehcacheService = ehcacheService;
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public User getLoginUser() {
        HttpServletRequest request = getRequest();
        String username = request.getSession().getAttribute(GlobalHandlerInterceptor.SESSION_KEY_USER).toString();
        return ehcacheService.get(username);
    }
}
