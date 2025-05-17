package com.jnu.capstone.controller;

import com.jnu.capstone.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gathering")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    @GetMapping("/{postId}/applicants")
    public ResponseEntity<?> getApplicants(
            @PathVariable int postId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<?> applicants = applicantService.getApplicants(postId, pageable);

        // 응답 데이터 생성
        Map<String, Object> response = Map.of(
                "status", "success",
                "data", applicants
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/apply")
    public ResponseEntity<?> applyForGathering(
            @PathVariable int postId,
            @RequestHeader(value = "User-Id", required = false) Integer userId,
            @RequestBody(required = false) Map<String, String> requestBody
    ) {
        try {
            // 사용자 ID가 없는 경우 에러 처리
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "User-Id 헤더가 필요합니다."
                ));
            }

            // 스터디 게시글인 경우 지원 문구 포함
            String applicationText = "";
            if (requestBody != null && requestBody.containsKey("application_text")) {
                applicationText = requestBody.get("application_text");
            }

            // 지원 처리
            applicantService.applyForGathering(postId, userId, applicationText);

            // 성공 응답
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "지원이 완료되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            // 예외 처리
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
