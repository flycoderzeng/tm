package com.tm.mockserver.controller;


import com.tm.mockserver.util.RestTemplateUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class MockTestController {

    @GetMapping(value = "/mock/test")
    public Object test() {
        final RestTemplate instance = RestTemplateUtils.getInstance("utf-8");
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accept", "application/vnd.api+json");
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        return instance.getForEntity("http://127.0.0.1:9081/api/v1/group", String.class, httpEntity);
    }

    @GetMapping(value = "/mock/test1")
    public Object test1() {
        final RestTemplate instance = RestTemplateUtils.getInstance("utf-8");
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accept", "application/vnd.api+json");
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        return instance.getForEntity("http://127.0.0.1:9081/api/v1/group/1", String.class, httpEntity);
    }
}
