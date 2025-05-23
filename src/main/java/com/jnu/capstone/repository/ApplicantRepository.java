package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Applicant;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    long countByPostAndIsAccepted(Post post, boolean isAccepted);
    Page<Applicant> findByPost_PostIdAndPost_BoardType(int postId, BoardType boardType, Pageable pageable);
    boolean existsByPostAndUser(Post post, User user);
}

