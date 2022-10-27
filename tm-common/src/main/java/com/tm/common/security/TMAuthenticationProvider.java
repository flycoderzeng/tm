package com.tm.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class TMAuthenticationProvider implements AuthenticationProvider {
    public static final String INVALID_USERNAME = "用户名不存在";
    public static final String INVALID_PASSWORD = "密码不正确";
    @Autowired
    private UserDetailsServiceImpl userService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = null;

        try {
            userDetails = userService.loadUserByUsername(userName);
        } catch (UsernameNotFoundException e) {
            // 不要抛出异常
            log.info(e.getMessage());
        }

        if(userDetails == null) {
            log.error("用户名: {} 不存在", userName);
            throw new BadCredentialsException(INVALID_USERNAME);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
        if(!bCryptPasswordEncoder.matches(password, userDetails.getPassword()) && !bCryptPasswordEncoder.matches(bCryptPasswordEncoder.encode(password), userDetails.getPassword())) {
            log.error("密码: {} 不正确", password);
            throw new BadCredentialsException(INVALID_PASSWORD);
        }

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        // 构建返回的用户登录成功的token
        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
