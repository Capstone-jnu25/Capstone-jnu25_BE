package com.jnu.capstone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnu.capstone.dto.AiResponseDto;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.SecondhandBoard;
import com.jnu.capstone.repository.LostBoardRepository;
import com.jnu.capstone.repository.PostKeywordRepository;
import com.jnu.capstone.repository.PostRepository;
import com.jnu.capstone.repository.SecondhandBoardRepository;
import com.jnu.capstone.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiRecommendService {

    private final SecondhandBoardRepository secondhandBoardRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final PostRepository postRepository;
    private final LostBoardRepository lostBoardRepository;

    public AiResponseDto sendToAiServer(Post post, MultipartFile newImage) {
        System.out.println("🔥 sendToAiServer 호출됨");
        try {
            int campusId = post.getCampus().getCampusId();
            BoardType boardType = post.getBoardType();

            // 1. 기존 게시글 20개 가져오기
            List<Post> recentPosts = postRepository
                    .findTop20ByCampusIdAndBoardTypeOrderByPostIdDesc(campusId, boardType)
                    .stream()
                    .filter(p -> p.getPostId() != post.getPostId())  // 🔥 현재 작성 중인 게시글 제외
                    .limit(20) // 제외하고 다시 20개로 잘라줌
                    .toList();

            // 2. existingPosts JSON 생성
            List<Map<String, Object>> existingPostList = recentPosts.stream().map(p -> {
                List<String> keywords = postKeywordRepository.findKeywordsByPostId(p.getPostId());

                return Map.of("postId", p.getPostId(), "keywords", keywords);
            }).toList();

            ObjectMapper objectMapper = new ObjectMapper();
            String existingPostsJson = objectMapper.writeValueAsString(existingPostList);

            // 3. existingImages 추출
            List<MultipartFile> existingImages = new ArrayList<>();
            for (Post p : recentPosts) {
                String imageUrl = extractImageUrlFromPost(p);
                if (imageUrl != null && !imageUrl.isBlank()) {
                    MultipartFile file = ImageUtil.urlToMultipart(imageUrl);
                    // ✅ 여기에 로그 추가
                    System.out.println("🔥 name = " + file.getName());  // → existingImages
                    System.out.println("🔥 originalFilename = " + file.getOriginalFilename());  // → image.jpg
                    System.out.println("🔥 contentType = " + file.getContentType());  // → image/jpeg

                    existingImages.add(file);
                }
            }

            if (existingImages.isEmpty()) {
                throw new IllegalStateException("AI 서버 요청 실패: existingImages는 1개 이상 필요합니다.");
            }

            // 4. newPost JSON 생성 - null 안전하게 처리
            Map<String, Object> newPostMap = new HashMap<>();
            newPostMap.put("postId", post.getPostId());
            newPostMap.put("title", post.getTitle());
            newPostMap.put("contents", post.getContents());

            String place = extractPlaceFromPost(post);
            if (place != null) newPostMap.put("place", place);

            Integer price = extractPriceFromPost(post);
            if (price != null) newPostMap.put("price", price);

            Boolean lost = extractLostStatusFromPost(post);
            if (lost != null) newPostMap.put("lost", lost);

            Double lat = extractLatitude(post);
            if (lat != null) newPostMap.put("lostLatitude", lat);

            Double lng = extractLongitude(post);
            if (lng != null) newPostMap.put("lostLongitude", lng);

            String newPostJson = objectMapper.writeValueAsString(newPostMap);


            // ✅ ✅ ✅ 여기서 로그 찍기!
            System.out.println("🧾 [AI 요청 데이터 로그]");
            System.out.println("newPostJson = " + newPostJson);
            System.out.println("existingPostsJson = " + existingPostsJson);
            System.out.println("existingImages.size = " + existingImages.size());
            System.out.println("newImage is null? " + (newImage == null));

            // (추가 로그 추천)
            for (Map<String, Object> item : existingPostList) {
                System.out.println("📝 postId = " + item.get("postId") + ", keywords = " + item.get("keywords"));
            }

            // 5. HTTP 요청 구성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("newPost", new HttpEntity<>(newPostJson, headers));
            body.add("newImage", new HttpEntity<>(newImage.getResource()));
            body.add("existingPosts", new HttpEntity<>(existingPostsJson, headers));
            for (MultipartFile file : existingImages) {
                HttpHeaders imageHeaders = new HttpHeaders();
                imageHeaders.setContentType(MediaType.IMAGE_JPEG); // 이미지가 JPEG이면
                HttpEntity<Resource> fileEntity = new HttpEntity<>(file.getResource(), imageHeaders);
                body.add("existingImages", fileEntity);
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(
                    "http://15.164.221.156:8000/ai/recommend",
                    requestEntity,
                    AiResponseDto.class
            );

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AI 서버 요청 실패", e);
        }
    }


    private String extractImageUrlFromPost(Post post) {
        if (post.getBoardType() == BoardType.LOST) {
            return post.getLostBoard() != null ? post.getLostBoard().getPhoto() : null;
        } else if (post.getBoardType() == BoardType.SECONDHAND) {
            return post.getSecondhandBoard() != null ? post.getSecondhandBoard().getPhoto() : null;
        }
        return null;
    }

    private String extractPlaceFromPost(Post post) {
        if (post.getBoardType() == BoardType.LOST) {
            return post.getLostBoard() != null ? post.getLostBoard().getPlace() : null;
        } else if (post.getBoardType() == BoardType.SECONDHAND) {
            return post.getSecondhandBoard() != null ? post.getSecondhandBoard().getPlace() : null;
        }
        return null;
    }

    private Integer extractPriceFromPost(Post post) {
        return post.getBoardType() == BoardType.SECONDHAND && post.getSecondhandBoard() != null
                ? post.getSecondhandBoard().getPrice() : null;
    }

    private Boolean extractLostStatusFromPost(Post post) {
        return post.getBoardType() == BoardType.LOST && post.getLostBoard() != null
                ? post.getLostBoard().isLost() : null;
    }

    private Double extractLatitude(Post post) {
        return post.getBoardType() == BoardType.LOST && post.getLostBoard() != null
                ? post.getLostBoard().getLostLatitude() : null;
    }

    private Double extractLongitude(Post post) {
        return post.getBoardType() == BoardType.LOST && post.getLostBoard() != null
                ? post.getLostBoard().getLostLongitude() : null;
    }


    public AiResponseDto sendToAiServer(Post dummyPost, MultipartFile newImage,
                                        List<Map<String, Object>> existingPosts,
                                        List<MultipartFile> existingImages) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String newPostJson = objectMapper.writeValueAsString(
                    Map.of("postId", dummyPost.getPostId(), "title", dummyPost.getTitle(), "contents", dummyPost.getContents())
            );
            String existingPostsJson = objectMapper.writeValueAsString(existingPosts);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("newPost", new HttpEntity<>(newPostJson, headers));
            body.add("newImage", new HttpEntity<>(newImage.getResource()));
            body.add("existingPosts", new HttpEntity<>(existingPostsJson, headers));
            for (MultipartFile file : existingImages) {
                body.add("existingImages", new HttpEntity<>(file.getResource()));
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(
                    "http://15.164.221.156:8000/ai/recommend",
                    requestEntity,
                    AiResponseDto.class
            );

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AI 서버 요청 실패", e);
        }
    }
}
