package com.jnu.capstone.service.impl;

import com.jnu.capstone.dto.EmailStatusRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${univcert.api.key}")
    private String univCertApiKey;

    public boolean checkEmailVerified(EmailStatusRequestDto requestDto) {
        String url = "https://univcert.com/api/v1/status";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                "{\"key\": \"%s\", \"email\": \"%s\"}",
                univCertApiKey, requestDto.getEmail()
        );

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
        );

        // UnivCert 응답에서 "success": true 확인
        return response.getBody().contains("\"success\":true");
    }
}
