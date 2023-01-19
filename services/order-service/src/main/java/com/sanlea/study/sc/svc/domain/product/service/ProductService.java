package com.sanlea.study.sc.svc.domain.product.service;

import com.sanlea.study.sc.common.starter.microservice_api_client.portal.PortalAPI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Product service
 *
 * @author kut
 */
//@PortalAPI
@PortalAPI
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
