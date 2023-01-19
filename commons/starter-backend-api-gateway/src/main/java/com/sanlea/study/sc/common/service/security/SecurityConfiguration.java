package com.sanlea.study.sc.common.service.security;

import com.sanlea.study.sc.common.service.security.portal.PortalApiHandlerInterceptor;
import com.sanlea.study.sc.common.service.security.service.ServiceApiAccessTokenManager;
import com.sanlea.study.sc.common.service.security.service.ServiceApiHandlerInterceptor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
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
@ImportAutoConfiguration(classes = {
        ServiceApiConfiguration.class
})
public class SecurityConfiguration implements WebMvcConfigurer {

    @Bean
    public SecurityFeignRequestInterceptor buildFeignRequestInterceptor(
            ServiceApiAccessTokenManager serviceApiAccessTokenManager
    ) {
        return new SecurityFeignRequestInterceptor(serviceApiAccessTokenManager);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(buildPortalApiHandlerInterceptor());
        registry.addInterceptor(buildServiceApiHandlerInterceptor());
    }

    @Bean
    public PortalApiHandlerInterceptor buildPortalApiHandlerInterceptor() {
        return new PortalApiHandlerInterceptor();
    }

    @Bean
    public ServiceApiHandlerInterceptor buildServiceApiHandlerInterceptor() {
        return new ServiceApiHandlerInterceptor();
    }
}
