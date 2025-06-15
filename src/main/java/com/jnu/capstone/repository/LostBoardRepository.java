package com.jnu.capstone.repository;

import com.jnu.capstone.entity.LostBoard;
import com.jnu.capstone.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LostBoardRepository extends JpaRepository<LostBoard, Integer> {

    // 분실물 or 습득물 필터링
    List<LostBoard> findByIsLost(boolean isLost);

    @Query("SELECT s FROM LostBoard s JOIN FETCH s.post WHERE s.postId = :postId")
    Optional<LostBoard> findWithPostByPostId(@Param("postId") int postId);

    @Query("SELECT s FROM LostBoard s JOIN FETCH s.post")
    List<LostBoard> findAllWithPost();

    @Query("SELECT l FROM LostBoard l JOIN FETCH l.post WHERE l.isLost = :isLost")
    List<LostBoard> findByIsLostWithPost(@Param("isLost") boolean isLost);

    @Query("SELECT l FROM LostBoard l JOIN FETCH l.post p WHERE l.isLost = :isLost AND p.campus.campusId = :campusId")
    List<LostBoard> findByCampusAndIsLost(@Param("campusId") int campusId, @Param("isLost") boolean isLost);

    @Query("SELECT l FROM LostBoard l JOIN FETCH l.post p " +
            "WHERE l.isLost = :isLost AND p.campus.campusId = :campusId AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.contents) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<LostBoard> searchByCampusAndTextAndType(@Param("campusId") int campusId,
                                                 @Param("query") String query,
                                                 @Param("isLost") boolean isLost);


    void deleteByPost(Post post);

    @Query("SELECT l FROM LostBoard l " +
            "JOIN FETCH l.post p " +
            "JOIN FETCH p.user " +
            "WHERE p.postId IN :postIds")
    List<LostBoard> findWithPostByPostIds(@Param("postIds") List<Integer> postIds);

}
