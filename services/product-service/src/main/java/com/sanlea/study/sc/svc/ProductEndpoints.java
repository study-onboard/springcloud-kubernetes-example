package com.sanlea.study.sc.svc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/portal/products")
@Slf4j
@RefreshScope
public class ProductEndpoints {

    @Value("${welcome.message}")
    private String message;

    @GetMapping
    public List<String> findAll() {
        var products = new ArrayList<String>();
        products.add("HuaWei Mate 9");
        products.add("iPhone 14");

        log.info("Welcome message: {}", message);
        return products;
    }
}
