package com.sanlea.study.sc.common.starter.microservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanlea.study.sc.common.starter.microservice.MicroserviceApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * Portal API handler interceptor
 *
 * @author kut
 */
@Slf4j
public class PortalApiHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper jsonMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // clear headers
        if (!isPortalApiEndpoint(request)) {
            return true;
        }

        var handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation(ValidatePortalApiAuth.class) &&
                StringUtils.isEmpty(request.getHeader("X-PAPI-ACCESS-TOKEN"))) {
            try {
                response.setStatus(403);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                var error = new MicroserviceApiError("PAPI.ACCESS_DENIED", "Invalid PAPI access token");
                response.getOutputStream().write(
                        jsonMapper.writeValueAsString(error).getBytes(StandardCharsets.UTF_8)
                );

                return false;
            } catch (Exception e) {
                log.error("Unknown PAPI intercepter error", e);
                return false;
            }
        }
        return true;
    }

    /**
     * check if is portal API endpoint
     *
     * @param request request
     * @return result
     */
    private boolean isPortalApiEndpoint(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/portal/");
    }
}
