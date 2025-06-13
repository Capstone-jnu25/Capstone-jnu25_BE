
package com.jnu.capstone.controller;

import com.jnu.capstone.dto.MyPostSimpleDto;
import com.jnu.capstone.dto.PostResponseDto;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jnu.capstone.util.JwtTokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PostController(PostService postService, JwtTokenProvider jwtTokenProvider) {
        this.postService = postService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ✅ [추가] 내가 쓴 글을 게시판별로 그룹화해서 조회
    @GetMapping("/my-grouped")
    public ResponseEntity<Map<String, List<MyPostSimpleDto>>> getMyGroupedPosts(
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        Map<String, List<MyPostSimpleDto>> result = postService.getMyPostsGroupedByBoardType(userId);
        return ResponseEntity.ok(result);
    }

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable int postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable int postId, @RequestBody Post updatedPost) {
        try {
            Post post = postService.updatePost(postId, updatedPost);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable int postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPosts(
            @RequestHeader("Authorization") String token,
            @RequestParam String keyword,
            @RequestParam List<String> boardType,
            @RequestParam int page,
            @RequestParam int size) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postId"));

        List<BoardType> boardTypes = boardType.stream()
                .map(String::toUpperCase)
                .map(BoardType::valueOf)
                .toList();

        Page<Post> resultPage = postService.searchPostsByCampusAndKeyword(userId, keyword, boardTypes, pageable);

        List<PostResponseDto> dtoList = resultPage.stream().map(post -> {
            var g = post.getGatheringBoard();
            return new PostResponseDto(
                    post.getPostId(),
                    post.getTitle(),
                    post.getContents(),
                    g.getPlace(),
                    g.getMeetTime(),
                    g.getDueDate(),
                    g.getGender().name(),
                    g.getMaxParticipants(),
                    g.getCurrentParticipants(),
                    post.getBoardType().name(),
                    isClosed(g),
                    PostResponseDto.calculateDDay(g.getDueDate()),
                    post.getUser().getUserId()
            );
        }).toList();

        Map<String, Object> response = Map.of(
                "status", "success",
                "data", dtoList,
                "meta", Map.of(
                        "page", resultPage.getNumber(),
                        "size", resultPage.getSize(),
                        "totalElements", resultPage.getTotalElements()
                )
        );

        return ResponseEntity.ok(response);
    }

    // ✅ 여기 추가
    private boolean isClosed(com.jnu.capstone.entity.GatheringBoard g) {
        if (g.getDueDate().isBefore(java.time.LocalDate.now())) return true;
        return g.isAutomatic() && g.getCurrentParticipants() >= g.getMaxParticipants();
    }
}
