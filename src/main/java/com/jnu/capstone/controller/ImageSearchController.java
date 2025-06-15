package com.jnu.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnu.capstone.dto.AiResponseDto;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.repository.*;
import com.jnu.capstone.service.AiRecommendService;
import com.jnu.capstone.util.ImageUtil;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/image")
public class ImageSearchController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LostBoardRepository lostBoardRepository;
    private final SecondhandBoardRepository secondhandBoardRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final AiRecommendService aiRecommendService;

    @PostMapping
    public ResponseEntity<?> searchByImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("boardType") BoardType boardType,
            @RequestParam(value = "isLost", required = false) Boolean isLost,
            @RequestPart("newImage") MultipartFile newImage
    ) {

        try {
            int userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
            int campusId = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 없음")).getCampus().getCampusId();

            System.out.println("🔥 searchByImage 호출됨");

            // ✅ newImage 로그 추가
            System.out.println("📥 newImage 수신됨:");
            System.out.println("📎 파일명: " + newImage.getOriginalFilename());
            System.out.println("📏 파일 크기: " + newImage.getSize() + " bytes");
            System.out.println("🧾 ContentType: " + newImage.getContentType());

            List<Map<String, Object>> existingPostList = new ArrayList<>();
            List<MultipartFile> existingImages = new ArrayList<>();

            System.out.println("🔍 초기 existingPostList = " + existingPostList);
            System.out.println("🔍 초기 existingImages.size = " + existingImages.size());


            if (boardType == BoardType.SECONDHAND) {
                secondhandBoardRepository.findTop20ByCampusAndBoardTypeOrderByPostDesc(campusId)
                        .forEach(board -> {
                            List<String> keywords = postKeywordRepository.findKeywordsByPostId(board.getPostId());
                            existingPostList.add(Map.of("postId", board.getPostId(), "keywords", keywords));
                            if (board.getPhoto() != null && !board.getPhoto().isBlank()) {
                                existingImages.add(ImageUtil.urlToMultipart(board.getPhoto()));
                            }
                        });

            } else if (boardType == BoardType.LOST && isLost != null) {
                // 분실물 페이지에서 검색하는 경우 → 습득물 대상 (isLost = false)
                // 습득물 페이지에서 검색하는 경우 → 분실물 대상 (isLost = true)
                boolean targetIsLost = !isLost;

                lostBoardRepository.findByCampusAndIsLost(campusId, targetIsLost).stream()
                        .sorted(Comparator.comparing(lb -> lb.getPost().getPostId(), Comparator.reverseOrder()))
                        .limit(20)
                        .forEach(board -> {
                            List<String> keywords = postKeywordRepository.findKeywordsByPostId(board.getPostId());
                            existingPostList.add(Map.of("postId", board.getPostId(), "keywords", keywords));
                            if (board.getPhoto() != null && !board.getPhoto().isBlank()) {
                                existingImages.add(ImageUtil.urlToMultipart(board.getPhoto()));
                            }
                        });
            }

            System.out.println("📦 existingImages size = " + existingImages.size());
            for (int i = 0; i < existingImages.size(); i++) {
                MultipartFile img = existingImages.get(i);
                System.out.println("🖼️ Image " + (i + 1) + " → name: " + img.getOriginalFilename()
                        + ", size: " + img.getSize() + " bytes, type: " + img.getContentType());
            }

            if (existingImages.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "기존 이미지가 필요합니다."));
            }

            Post dummyPost = new Post();
            dummyPost.setPostId(-1);
            dummyPost.setTitle("이미지 검색용");
            dummyPost.setContents("이미지 검색");
            dummyPost.setBoardType(boardType);
            dummyPost.setCampus(userRepository.findById(userId).get().getCampus());

            AiResponseDto aiResponse = aiRecommendService.sendToAiServer(dummyPost, newImage, existingPostList, existingImages);

            List<Integer> postIds = aiResponse != null && aiResponse.getPostIds() != null
                    ? aiResponse.getPostIds()
                    : List.of();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "recommendedPostIds", postIds
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "이미지 검색 중 오류", "error", e.getMessage()));
        }
    }
}
