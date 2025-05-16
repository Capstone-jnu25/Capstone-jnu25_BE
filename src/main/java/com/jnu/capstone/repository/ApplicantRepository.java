package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Applicant;
import com.jnu.capstone.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    long countByPostAndIsAccepted(Post post, boolean isAccepted);
}
