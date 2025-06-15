package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.SecondhandBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecondhandBoardRepository extends JpaRepository<SecondhandBoard, Integer> {

    // 게시글 상세 조회용
    @Query("SELECT s FROM SecondhandBoard s JOIN FETCH s.post WHERE s.postId = :postId")
    Optional<SecondhandBoard> findWithPostByPostId(@Param("postId") int postId);

    // 전체 목록 조회용 (기존 코드에 없으면 아래 추가)
    @Query("SELECT s FROM SecondhandBoard s JOIN FETCH s.post")
    List<SecondhandBoard> findAllWithPost();

    // 특정 학교 게시글만 조회 (새로 추가한 기능)
    @Query("SELECT s FROM SecondhandBoard s JOIN FETCH s.post p WHERE p.campus.campusId = :campusId")
    List<SecondhandBoard> findAllByCampusId(@Param("campusId") int campusId);

    @Query("SELECT s FROM SecondhandBoard s JOIN FETCH s.post p " +
            "WHERE p.campus.campusId = :campusId AND p.isDeleted = false AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.contents) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<SecondhandBoard> searchByCampusAndQuery(@Param("campusId") int campusId,
                                                 @Param("query") String query);

    @Query("SELECT s FROM SecondhandBoard s " +
            "JOIN FETCH s.post p " +
            "WHERE p.campus.campusId = :campusId " +
            "AND p.boardType = 'SECONDHAND' " +
            "AND p.isDeleted = false " +
            "ORDER BY p.postId DESC")
    List<SecondhandBoard> findTop20ByCampusAndBoardTypeOrderByPostDesc(@Param("campusId") int campusId);


    void deleteByPost(Post post);
}

