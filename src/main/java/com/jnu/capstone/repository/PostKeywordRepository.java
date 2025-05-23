package com.jnu.capstone.repository;

import com.jnu.capstone.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostKeywordRepository extends JpaRepository<PostKeyword, String> {
    void deleteByKeywordTextAndPostId(String keywordText, int postId);
}
