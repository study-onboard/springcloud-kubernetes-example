package com.sanlea.study.sc.svc.api.portal.endpoints;

import com.sanlea.study.sc.common.starter.microservice_api_client.portal.PortalApiRequestHeadersHolder;
import com.sanlea.study.sc.svc.domain.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

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

    @Value("${welcome.message}")
    private String message;

    public OrderEndpoints(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
//    @ValidatePortalApiAuth
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

    @Data
    public static class CreateForm {
        @NotBlank(message = "show me the money")
        private String id;

        @NotEmpty
        private String name;
    }

    @PostMapping
    public String create(@Valid @RequestBody CreateForm form) {
        return form.id;
    }
}
