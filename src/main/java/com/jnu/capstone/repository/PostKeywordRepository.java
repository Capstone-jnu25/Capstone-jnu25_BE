package com.jnu.capstone.repository;

import com.jnu.capstone.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostKeywordRepository extends JpaRepository<PostKeyword, String> {

    @Query("SELECT pk.keywordText FROM PostKeyword pk WHERE pk.postId = :postId")
    List<String> findKeywordsByPostId(@Param("postId") int postId);

    void deleteByKeywordTextAndPostId(String keywordText, int postId);
}
