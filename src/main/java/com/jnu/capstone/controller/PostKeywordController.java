package com.jnu.capstone.controller;

import com.jnu.capstone.entity.PostKeyword;
import com.jnu.capstone.dto.PostKeywordCreateRequestDto;
import com.jnu.capstone.repository.PostKeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postkeywords")
public class PostKeywordController {

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @GetMapping
    public List<PostKeyword> getAllPostKeywords() {
        return postKeywordRepository.findAll();
    }

    @PostMapping
    public PostKeyword createPostKeyword(@RequestBody PostKeywordCreateRequestDto requestDto) {
        PostKeyword postKeyword = new PostKeyword();
        postKeyword.setKeywordText(requestDto.getKeywordText());  // 수정된 부분
        postKeyword.setPostId(requestDto.getPostId());
        return postKeywordRepository.save(postKeyword);
    }

    @DeleteMapping("/{keyword}/{postId}")
    public void deletePostKeyword(@PathVariable String keyword, @PathVariable int postId) {
        postKeywordRepository.deleteByKeywordTextAndPostId(keyword, postId);
    }
}
