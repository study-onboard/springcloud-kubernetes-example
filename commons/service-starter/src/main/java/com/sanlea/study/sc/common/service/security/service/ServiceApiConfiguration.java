package com.sanlea.study.sc.common.service.security.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Security MVC configurer
 *
 * @author kut
 */
@Configuration
public class ServiceApiConfiguration {

    @Bean
    public ServiceApiAccessTokenManager buildAccessTokenManager(RestTemplate restTemplate) {
        return new ServiceApiAccessTokenManager(restTemplate);
    }
}
