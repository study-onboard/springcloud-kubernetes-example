package com.sanlea.study.sc.common.starter.microservice;

import com.sanlea.study.sc.common.starter.microservice.security.SecurityConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class MicroserviceConfiguration {

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
