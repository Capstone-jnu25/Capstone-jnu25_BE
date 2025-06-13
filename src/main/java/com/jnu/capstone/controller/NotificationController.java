package com.jnu.capstone.controller;
import com.jnu.capstone.dto.NotificationLogDto;
import com.jnu.capstone.repository.NotificationLogRepository;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationLogRepository notificationLogRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMyNotifications(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<NotificationLogDto> result = notificationLogRepository.findByUser_UserId(userId).stream()
                .map(NotificationLogDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "data", result
                )
        );
    }
}
