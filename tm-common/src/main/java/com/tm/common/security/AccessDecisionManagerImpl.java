package com.tm.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
public class AccessDecisionManagerImpl implements AccessDecisionManager {

    public static final String ROLE_LOGIN = "ROLE_LOGIN";
    public static final String ROLE_ROOT_ADMIN = "ROLE_ROOT_ADMIN";
    public static final String NO_PERMISSION = "{\"code\": 401, \"message\": \"no permission\"}";

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {
        // 迭代器遍历目标url的权限列表
        for (ConfigAttribute ca : collection) {
            String needRole = ca.getAttribute();
            log.info("需要的角色：{}", needRole);
            if (ROLE_LOGIN.equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException(NO_PERMISSION);
                }
                return;
            }

            // 遍历当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                log.info(authority.getAuthority());
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
                if (authority.getAuthority().equals(ROLE_ROOT_ADMIN)) {
                    return;
                }
            }
        }

        // 执行到这里说明没有匹配到应有权限
        throw new AccessDeniedException(NO_PERMISSION);
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
