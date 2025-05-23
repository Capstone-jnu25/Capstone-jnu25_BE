package com.jnu.capstone.controller;

import com.jnu.capstone.dto.GroupDto;
import com.jnu.capstone.service.ApplicantService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {

    private final ApplicantService chatroomService; // 또는 ChatJoinService

    @GetMapping
    public ResponseEntity<?> getMyGroups(@RequestHeader("User-Id") int userId) {
        List<GroupDto> groups = chatroomService.getMyGroups(userId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", groups
        ));
    }
}

