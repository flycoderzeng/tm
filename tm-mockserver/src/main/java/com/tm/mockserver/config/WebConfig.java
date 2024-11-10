package com.tm.mockserver.config;

import com.tm.mockserver.service.CustomizeMockRequestHandleService;
import jakarta.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final CustomizeMockRequestHandleService customizeMockRequestHandleService;

    @Inject
    public WebConfig(CustomizeMockRequestHandleService customizeMockRequestHandleService) {
        this.customizeMockRequestHandleService = customizeMockRequestHandleService;
    }

    //新建一个拦截类注入到spring容器
    @Bean
    public HandlerInterceptor getGlobalInterceptor(){
        //返回自定义的拦截类
        return new GlobalHandlerInterceptor(customizeMockRequestHandleService);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getGlobalInterceptor());
    }
}