package com.jnu.capstone.controller;

import com.jnu.capstone.dto.MessageResponseDto;
import com.jnu.capstone.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatrooms")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{chattingRoomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable int chattingRoomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponseDto> messages = messageService.getMessages(chattingRoomId, pageable);

        // 응답 데이터 생성
        Map<String, Object> response = Map.of(
                "status", "success",
                "data", messages.getContent(),
                "pageNumber", messages.getNumber(),
                "pageSize", messages.getSize(),
                "totalElements", messages.getTotalElements(),
                "totalPages", messages.getTotalPages(),
                "last", messages.isLast()
        );

        return ResponseEntity.ok(response);
    }
}
