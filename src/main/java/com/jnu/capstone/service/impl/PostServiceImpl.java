package com.jnu.capstone.service.impl;

import com.jnu.capstone.dto.MyPostSimpleDto;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.repository.PostRepository;
import com.jnu.capstone.service.PostService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> getPostById(int postId) {
        return postRepository.findById(postId);
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(int postId, Post updatedPost) {
        if (postRepository.existsById(postId)) {
            updatedPost.setPostId(postId);
            return postRepository.save(updatedPost);
        } else {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }
    }

    @Override
    public void deletePost(int postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Map<String, List<MyPostSimpleDto>> getMyPostsGroupedByBoardType(int userId) {
        List<Post> posts = postRepository.findByUser_UserId(userId);

        return posts.stream()
                .map(post -> new MyPostSimpleDto(
                        post.getPostId(),
                        post.getTitle(),
                        post.getBoardType().name()
                ))
                .collect(Collectors.groupingBy(MyPostSimpleDto::getBoardType));
    }
}
