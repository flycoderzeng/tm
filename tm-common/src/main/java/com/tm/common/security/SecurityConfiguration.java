package com.tm.common.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsUtils;

import java.io.PrintWriter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //  启用方法级别的权限认证
@Order(-1)
public class SecurityConfiguration {
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=utf-8";
    public static final String LOGIN_SUCCESS = "{\"code\": 0, \"message\": \"login success\"}";
    public static final String LOGOUT_SUCCESS = "{\"code\": 0, \"message\": \"logout success\"}";
    public static final String JSESSIONID = "JSESSIONID";
    public static final String LOGIN_URI = "/login";
    public static final String SESSION = "SESSION";

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/tm/public/api/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TMAccessDeniedHandler tmAccessDeniedHandler,
                                           FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource,
                                           AccessDecisionManagerImpl accessDecisionManager) throws Exception {
        log.info("配置 HttpSecurity");
        return http.headers().frameOptions().disable()
                .and().cors().and().csrf().disable().exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and().authorizeRequests()
                // 处理跨域请求中的Preflight请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers("/tm/public/api/**").permitAll()
                .and().httpBasic()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter out = response.getWriter();
                    out.write(AccessDecisionManagerImpl.NO_PERMISSION);
                    out.flush();
                    out.close();
                }).and().authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                        o.setAccessDecisionManager(accessDecisionManager);
                        return o;
                    }
                })
                .requestMatchers(LOGIN_URI).permitAll().requestMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated().and()  // 其他地址的访问均需验证权限
                .formLogin().loginProcessingUrl(LOGIN_URI).failureHandler((request, response, authException) -> {
                    response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter out = response.getWriter();
                    out.write(AccessDecisionManagerImpl.NO_PERMISSION);
                    out.flush();
                    out.close();
                }).permitAll().successHandler((request, response, authException) -> {
                    response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
                    response.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = response.getWriter();
                    out.write(LOGIN_SUCCESS);
                    out.flush();
                    out.close();
                }).and().exceptionHandling().accessDeniedHandler(tmAccessDeniedHandler)
                .and()
                .logout().
                //退出成功，返回json
                        logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
                    response.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = response.getWriter();
                    out.write(LOGOUT_SUCCESS);
                    out.flush();
                    out.close();
                }).permitAll().invalidateHttpSession(true)
                .deleteCookies(JSESSIONID, SESSION)
                .and().sessionManagement().maximumSessions(10).and()
                .and().build();
    }
}
