package com.sanlea.study.sc.svc.api.portal.endpoints;

import com.sanlea.study.sc.common.service.security.portal.PortalApiRequestHeadersHolder;
import com.sanlea.study.sc.common.service.security.service.ServiceApiAccessTokenManager;
import com.sanlea.study.sc.svc.domain.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Order endpoints
 *
 * @author kut
 */
@RestController
@RequestMapping("/portal/orders")
@Slf4j
@RefreshScope
public class OrderEndpoints {
    private final ProductService productService;
    private final ServiceApiAccessTokenManager accessTokenManager;


    @Value("${welcome.message}")
    private String message;

    public OrderEndpoints(ProductService productService, ServiceApiAccessTokenManager accessTokenManager) {
        this.productService = productService;
        this.accessTokenManager = accessTokenManager;
    }

    @GetMapping
    public List<String> orderList() {
        var orders = new ArrayList<String>();
        orders.add("ID00001");
        orders.add("ID00002");

        var products = productService.findAll();
        log.info("Received product list: {}", products);

        log.info("Read message from config: {}", message);

        var headers = PortalApiRequestHeadersHolder.getHeaders();
        log.info("token: {}", headers.getHeader("X-ACCESS-TOKEN"));

        return orders;
    }
}
