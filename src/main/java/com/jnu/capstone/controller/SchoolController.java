package com.jnu.capstone.controller;

import com.jnu.capstone.dto.SchoolCreateRequestDto;
import com.jnu.capstone.entity.School;
import com.jnu.capstone.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    // 캠퍼스 생성
    @PostMapping
    public ResponseEntity<School> createSchool(@RequestBody SchoolCreateRequestDto requestDto) {
        School savedSchool = schoolService.createSchool(requestDto);
        return ResponseEntity.ok(savedSchool);
    }
}
