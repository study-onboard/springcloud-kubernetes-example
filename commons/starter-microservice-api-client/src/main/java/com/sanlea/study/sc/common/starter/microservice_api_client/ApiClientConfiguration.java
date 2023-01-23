package com.sanlea.study.sc.common.starter.microservice_api_client;

import com.sanlea.study.sc.common.starter.microservice_api_client.backend.BackendApiAccessTokenManager;
import com.sanlea.study.sc.common.starter.microservice_api_client.backend.BackendApiFeignRequestInterceptor;
import com.sanlea.study.sc.common.starter.microservice_api_client.portal.PortalApiFeignRequestInterceptor;
import com.sanlea.study.sc.common.starter.microservice_api_client.portal.PortalApiHandlerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * API client configuration
 *
 * @author kut
 */
@Configuration
public class ApiClientConfiguration implements WebMvcConfigurer {

    /**
     * build rest template
     *
     * @return rest template
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate buildRestTemplate() {
        return new RestTemplate();
    }

    @Bean("microservice_api_client.BackendApiFeignRequestInterceptor")
    public BackendApiFeignRequestInterceptor buildBackendApiFeignRequestInterceptor(
            BackendApiAccessTokenManager backendApiAccessTokenManager
    ) {
        return new BackendApiFeignRequestInterceptor(backendApiAccessTokenManager);
    }


    @Bean("microservice_api_client.PortalApiFeignRequestInterceptor")
    public PortalApiFeignRequestInterceptor buildPortalApiFeignRequestInterceptor() {
        return new PortalApiFeignRequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(buildPortalApiHandlerInterceptor());
    }


    @Bean("microservice_api_client.PortalApiHandlerInterceptor")
    public PortalApiHandlerInterceptor buildPortalApiHandlerInterceptor() {
        return new PortalApiHandlerInterceptor();
    }


    @Bean("microservice_api_client.BackendApiAccessTokenManager")
    public BackendApiAccessTokenManager buildAccessTokenManager(RestTemplate restTemplate) {
        return new BackendApiAccessTokenManager(restTemplate);
    }
}
