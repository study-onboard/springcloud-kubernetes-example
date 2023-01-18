package com.sanlea.study.sc.common.service.security;

import com.sanlea.study.sc.common.service.security.portal.PortalAPI;
import com.sanlea.study.sc.common.service.security.portal.PortalApiRequestHeadersHolder;
import com.sanlea.study.sc.common.service.security.service.ServiceAPI;
import com.sanlea.study.sc.common.service.security.service.ServiceApiAccessTokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Security feign request interceptor
 *
 * @author kut
 */
public class SecurityFeignRequestInterceptor implements RequestInterceptor {

    private final ServiceApiAccessTokenManager serviceApiAccessTokenManager;

    public SecurityFeignRequestInterceptor(ServiceApiAccessTokenManager serviceApiAccessTokenManager) {
        this.serviceApiAccessTokenManager = serviceApiAccessTokenManager;
    }

    @Override
    public void apply(RequestTemplate template) {

        var clazz = template.feignTarget().type();
        if (clazz.isAnnotationPresent(ServiceAPI.class)) {
            var sapi = clazz.getAnnotation(ServiceAPI.class);
            var accessToken = serviceApiAccessTokenManager.getAccessToken(
                    sapi.applicationId(),
                    sapi.service()
            );
            template.header("X-APPLICATION-ID", serviceApiAccessTokenManager.getApplicationId());
            template.header("X-SAPI-ACCESS-TOKEN", accessToken);
        }

        if (clazz.isAnnotationPresent(PortalAPI.class)) {
            var headers = PortalApiRequestHeadersHolder.getHeaders();
            template.headers(headers);
        }
    }
}
