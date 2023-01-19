package com.sanlea.study.sc.common.starter.microservice_api_client.portal;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Portal API feign request interceptor
 *
 * @author kut
 */
public class PortalApiFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        var clazz = template.feignTarget().type();
        if (clazz.isAnnotationPresent(PortalAPI.class)) {
            var headers = PortalApiRequestHeadersHolder.getHeaders();
            template.headers(headers);
        }
    }
}
