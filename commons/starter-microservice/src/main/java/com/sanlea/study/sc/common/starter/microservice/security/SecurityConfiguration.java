package com.sanlea.study.sc.common.starter.microservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration
 *
 * @author kut
 */
@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(buildPortalApiHandlerInterceptor());
    }

    @Bean("microservice.PortalApiHandlerInterceptor")
    public PortalApiHandlerInterceptor buildPortalApiHandlerInterceptor() {
        return new PortalApiHandlerInterceptor();
    }
}
