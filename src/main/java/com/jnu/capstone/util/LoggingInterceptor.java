//package com.jnu.capstone.util; // 패키지는 프로젝트 구조에 맞게 변경
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//
//public class LoggingInterceptor implements ClientHttpRequestInterceptor {
//    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
//
//    @Override
//    public ClientHttpResponse intercept(
//            HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        // 요청 로그
//        log.info("Request to: {}", request.getURI());
//        log.info("Request method: {}", request.getMethod());
//        log.info("Request headers: {}", request.getHeaders());
//        log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
//
//        // 실제 요청 실행
//        ClientHttpResponse response = execution.execute(request, body);
//
//        // 응답 로그
//        log.info("Response status: {}", response.getStatusCode());
//        log.info("Response headers: {}", response.getHeaders());
//        StringBuilder responseBody = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                responseBody.append(line);
//            }
//        }
//        log.info("Response body: {}", responseBody);
//        return response;
//    }
//}
