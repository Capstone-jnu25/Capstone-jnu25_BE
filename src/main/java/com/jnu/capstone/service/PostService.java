package com.jnu.capstone.service;

import com.jnu.capstone.dto.MyPostSimpleDto;
import com.jnu.capstone.entity.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {
    List<Post> getAllPosts();
    Optional<Post> getPostById(int postId);
    Post createPost(Post post);
    Post updatePost(int postId, Post updatedPost);
    void deletePost(int postId);

    Map<String, List<MyPostSimpleDto>> getMyPostsGroupedByBoardType(int userId);
}
