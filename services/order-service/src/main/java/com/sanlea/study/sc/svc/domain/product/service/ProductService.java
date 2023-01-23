package com.sanlea.study.sc.svc.domain.product.service;

import com.sanlea.study.sc.common.starter.microservice_api_client.backend.BackendAPI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Product service
 *
 * @author kut
 */
//@PortalAPI
//@PortalAPI
@BackendAPI(
        applicationId = "xxxx",
        service = "yyyy"
)
@FeignClient(
        name = "order-service",
        url = "${services.product.url}",
        path = "/api/portal/products"
)
public interface ProductService {

    /**
     * find all
     *
     * @return all product names
     */
    @GetMapping
    List<String> findAll();
}
