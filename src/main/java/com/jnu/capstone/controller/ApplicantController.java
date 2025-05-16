package com.jnu.capstone.controller;

import com.jnu.capstone.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Page<?> applicants = applicantService.getApplicants(postId, PageRequest.of(page, size));
        return ResponseEntity.ok().body(applicants);
    }
}
