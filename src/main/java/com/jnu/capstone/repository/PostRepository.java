package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // ✅ 사용자 ID로 게시글 목록 조회
    List<Post> findByUser_UserId(int userId);
}
