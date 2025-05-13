package com.jnu.capstone.service;

import com.jnu.capstone.entity.Applicant;
import com.jnu.capstone.repository.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;

    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    // 모든 신청자 조회
    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
    }

    // 특정 신청자 조회
    public Optional<Applicant> getApplicantById(int applicantId) {
        return applicantRepository.findById(applicantId);
    }

    // 신청자 생성
    public Applicant createApplicant(Applicant applicant) {
        return applicantRepository.save(applicant);
    }

    // 신청자 삭제
    public void deleteApplicant(int applicantId) {
        applicantRepository.deleteById(applicantId);
    }
}