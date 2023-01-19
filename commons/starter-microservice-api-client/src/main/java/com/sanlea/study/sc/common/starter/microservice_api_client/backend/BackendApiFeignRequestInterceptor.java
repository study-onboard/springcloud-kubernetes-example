package com.sanlea.study.sc.common.starter.microservice_api_client.backend;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Backend API feign request interceptor
 *
 * @author kut
 */
public class BackendApiFeignRequestInterceptor implements RequestInterceptor {

    private final BackendApiAccessTokenManager backendApiAccessTokenManager;

    public BackendApiFeignRequestInterceptor(BackendApiAccessTokenManager backendApiAccessTokenManager) {
        this.backendApiAccessTokenManager = backendApiAccessTokenManager;
    }

    @Override
    public void apply(RequestTemplate template) {

        var clazz = template.feignTarget().type();
        if (clazz.isAnnotationPresent(BackendAPI.class)) {
            var sapi = clazz.getAnnotation(BackendAPI.class);
            var accessToken = backendApiAccessTokenManager.getAccessToken(
                    sapi.applicationId(),
                    sapi.service()
            );
            template.header("X-APPLICATION-ID", backendApiAccessTokenManager.getApplicationId());
            template.header("X-SAPI-ACCESS-TOKEN", accessToken);
        }
    }
}
