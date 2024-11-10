package com.tm.common.security;


import com.tm.common.base.mapper.RoleMapper;
import com.tm.common.base.mapper.UserMapper;
import com.tm.common.base.model.User;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final String INVALID_USERNAME = "用户名不存在";
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Inject
    public UserDetailsServiceImpl(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        log.info("登录的用户名是：{}", s);
        User user = userMapper.getUserByNameWithPassword(s);
        if(user == null) {
            log.error("登录的用户：{} 在users表中不存在", s);
            throw new UsernameNotFoundException(INVALID_USERNAME);
        }
        return new UserDetailsImpl(user, roleMapper.getUserRoles(user.getId()));
    }
}
