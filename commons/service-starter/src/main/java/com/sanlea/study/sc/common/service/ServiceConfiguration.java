package com.sanlea.study.sc.common.service;

import com.sanlea.study.sc.common.service.security.SecurityConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Service configuration
 *
 * @author kut
 */
@Configuration
@ImportAutoConfiguration(classes = {
        SecurityConfiguration.class
})
public class ServiceConfiguration {

    /**
     * build rest template
     *
     * @return rest template
     */
    @Bean
    public RestTemplate buildRestTemplate() {
        return new RestTemplate();
    }
}
