package com.jnu.capstone.controller;

import com.jnu.capstone.service.ApplicantService;
import com.jnu.capstone.util.JwtTokenProvider;
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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private int extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 인증 토큰입니다.");
        }
        String token = authHeader.substring(7); // "Bearer " 이후 토큰만
        return jwtTokenProvider.getUserIdFromToken(token);
    }

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
                "data", Map.of(
                        "content", applicants.getContent(),
                        "pageNumber", applicants.getNumber(),
                        "pageSize", applicants.getSize(),
                        "totalElements", applicants.getTotalElements(),
                        "totalPages", applicants.getTotalPages(),
                        "last", applicants.isLast(),
                        "first", applicants.isFirst()
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/apply")
    public ResponseEntity<?> applyForGathering(
            @PathVariable int postId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody(required = false) Map<String, String> requestBody
    ) {
        try {
            int userId = extractUserId(authHeader);

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

    @PostMapping("/applicants/{applicantId}/accept")
    public ResponseEntity<?> acceptApplicant(
            @PathVariable int applicantId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            int userId = extractUserId(authHeader);
            applicantService.acceptApplicant(applicantId, userId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "지원자가 수락되었습니다. 채팅방에 초대 완료."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/applicants/{applicantId}")
    public ResponseEntity<?> deleteApplicant(
            @PathVariable int applicantId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            int userId = extractUserId(authHeader);
            applicantService.deleteApplicant(applicantId, userId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "지원자가 삭제되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}