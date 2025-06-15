//package com.jnu.capstone.config;
//
//import com.jnu.capstone.util.LoggingInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.BufferingClientHttpRequestFactory;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Configuration
//public class RestTemplateConfig {
//
//    @Bean(name = "customRestTemplate") // ✅ 이름을 customRestTemplate으로 지정
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate(
//                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
//        );
//        restTemplate.setInterceptors(List.of(new LoggingInterceptor()));
//        return restTemplate;
//    }
//}
