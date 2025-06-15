package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // ✅ 사용자 ID로 게시글 목록 조회
    List<Post> findByUser_UserId(int userId);
    @EntityGraph(attributePaths = {"gatheringBoard"})
    @Query("SELECT p FROM Post p WHERE " +
            "p.campus.campusId = :campusId AND " +
            "p.isDeleted = false AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.contents) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "p.boardType IN :boardTypes")
    Page<Post> findByCampusAndKeywordAndBoardType(@Param("campusId") int campusId,
                                                  @Param("keyword") String keyword,
                                                  @Param("boardTypes") List<BoardType> boardTypes,
                                                  Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.campus.campusId = :campusId AND p.boardType = :boardType ORDER BY p.postId DESC")
    List<Post> findTop20ByCampusIdAndBoardTypeOrderByPostIdDesc(@Param("campusId") int campusId, @Param("boardType") BoardType boardType);

    @Query("SELECT p FROM Post p JOIN FETCH p.lostBoard WHERE p.postId = :postId")
    Optional<Post> findWithLostBoardByPostId(@Param("postId") int postId);


}
