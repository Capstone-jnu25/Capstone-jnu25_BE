package com.jnu.capstone.service;

import com.jnu.capstone.entity.Post;
import com.jnu.capstone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 게시글 조회
    public Optional<Post> getPostById(int postId) {
        return postRepository.findById(postId);
    }

    // 게시글 생성
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // 게시글 수정
    public Post updatePost(int postId, Post updatedPost) {
        if (postRepository.existsById(postId)) {
            updatedPost.setPostId(postId);
            return postRepository.save(updatedPost);
        } else {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }
    }

    // 게시글 삭제
    public void deletePost(int postId) {
        postRepository.deleteById(postId);
    }
}
