package com.jnu.capstone.controller;

import com.jnu.capstone.dto.EmailStatusRequestDto;
import com.jnu.capstone.service.impl.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/status")
    public ResponseEntity<?> checkEmailStatus(@RequestBody EmailStatusRequestDto requestDto) {
        boolean isVerified = emailVerificationService.checkEmailVerified(requestDto);

        if (isVerified) {
            return ResponseEntity.ok("이메일이 인증되어 있습니다.");
        } else {
            return ResponseEntity.status(400).body("이메일 인증이 완료되지 않았습니다.");
        }
    }

}
