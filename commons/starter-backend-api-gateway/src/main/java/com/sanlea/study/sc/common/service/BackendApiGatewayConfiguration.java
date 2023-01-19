package com.sanlea.study.sc.common.service;

import com.sanlea.study.sc.common.service.security.SecurityConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Backend API gateway configuration
 *
 * @author kut
 */
@Configuration
@ImportAutoConfiguration(classes = {
        SecurityConfiguration.class
})
public class BackendApiGatewayConfiguration {

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
}
