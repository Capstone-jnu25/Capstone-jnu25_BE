package com.jnu.capstone.repository;

import com.jnu.capstone.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmailAndVerifiedTrue(String email);
    Optional<EmailVerification> findByEmail(String email);
}
