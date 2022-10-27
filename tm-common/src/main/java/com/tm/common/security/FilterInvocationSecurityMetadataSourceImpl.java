package com.tm.common.security;


import com.tm.common.base.mapper.RightMapper;
import com.tm.common.base.mapper.RoleMapper;
import com.tm.common.base.model.Right;
import com.tm.common.base.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
    public static final String ROLE_LOGIN = "ROLE_LOGIN";
    @Autowired
    private RightMapper rightMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o)  {
        // 接收用户请求的地址，返回访问该地址需要的所有权限
        String requestUri = ((FilterInvocation)o).getRequestUrl();
        log.info("请求的地址是: {}" ,requestUri);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetailsImpl) {
            log.info("登录的用户是: {}" ,((UserDetailsImpl) principal).getUser().getUsername());
        }

        // 如果是登录和退出就不需要权限
        if("/login".equals(requestUri) || "/logout".equals(requestUri)) {
            return Collections.emptyList();
        }

        Right right = rightMapper.findByURI(requestUri);
        // 如果没有匹配的url则说明大家都可以访问
        if(right == null) {
            return SecurityConfig.createList(ROLE_LOGIN);
        }
        log.info(right.getName());
        // 将right所需要到的roles按框架要求封装返回
        List<Role> roles = roleMapper.getRightRelatedRoles(right.getId());
        int size = roles.size();
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = roles.get(i).getName();
        }
        log.info(StringUtils.join(values, ","));

        return SecurityConfig.createList(values);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return Collections.emptyList();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
