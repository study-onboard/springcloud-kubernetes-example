package com.sanlea.study.sc.svc.api.portal.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/portal/products")
@Slf4j
public class ProductEndpoints {

    @GetMapping
    public List<String> findAll(@RequestHeader(value = "X-ACCESS-TOKEN", required = false) String accessToken) {
        log.info("products received access token: {}", accessToken);
        var result = new ArrayList<String>();
        result.add("iPhone 13");
        result.add("HuaWei Mate 9");
        return result;
    }
}
