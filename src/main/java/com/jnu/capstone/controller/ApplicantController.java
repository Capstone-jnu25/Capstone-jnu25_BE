package com.jnu.capstone.controller;

import com.jnu.capstone.dto.ApplicantResponseDto;
import com.jnu.capstone.entity.Applicant;
import com.jnu.capstone.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;

    @Autowired
    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    // 모든 신청자 조회
    @GetMapping
    public ResponseEntity<List<ApplicantResponseDto>> getAllApplicants() {
        List<ApplicantResponseDto> applicants = applicantService.getAllApplicants()
                .stream()
                .map(applicant -> new ApplicantResponseDto(
                        applicant.getApplicantId(),
                        applicant.getUserId(),
                        applicant.getApplicationText(),
                        applicant.isAccepted()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    // 특정 신청자 조회
    @GetMapping("/{applicantId}")
    public ResponseEntity<ApplicantResponseDto> getApplicantById(@PathVariable int applicantId) {
        Optional<Applicant> applicant = applicantService.getApplicantById(applicantId);
        return applicant.map(value -> new ResponseEntity<>(
                        new ApplicantResponseDto(
                                value.getApplicantId(),
                                value.getUserId(),
                                value.getApplicationText(),
                                value.isAccepted()
                        ), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 신청자 생성
    @PostMapping
    public ResponseEntity<ApplicantResponseDto> createApplicant(@RequestBody Applicant applicant) {
        Applicant createdApplicant = applicantService.createApplicant(applicant);
        ApplicantResponseDto responseDto = new ApplicantResponseDto(
                createdApplicant.getApplicantId(),
                createdApplicant.getUserId(),
                createdApplicant.getApplicationText(),
                createdApplicant.isAccepted()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 신청자 삭제
    @DeleteMapping("/{applicantId}")
    public ResponseEntity<Void> deleteApplicant(@PathVariable int applicantId) {
        applicantService.deleteApplicant(applicantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
