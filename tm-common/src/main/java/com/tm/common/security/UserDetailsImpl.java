package com.tm.common.security;

import com.tm.common.base.model.Role;
import com.tm.common.base.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;

    // 包含用户对应的所有role，在使用时调用着给对象注入roles
    private List<Role> roles;

    private User user;

    public UserDetailsImpl() {

    }

    public UserDetailsImpl(User user) {
        this.user = user;
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public UserDetailsImpl(User user, List<Role> roles) {
        this.user = user;
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = roles;
    }

    public boolean hasRole(String roleName) {
        if(roles == null) {
            return false;
        }
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
