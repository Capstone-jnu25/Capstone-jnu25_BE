package com.jnu.capstone.service;

import com.jnu.capstone.dto.LostBoardCreateRequestDto;
import com.jnu.capstone.dto.LostBoardDto;
import com.jnu.capstone.dto.LostItemMapResponseDto;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.LostBoard;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.LostBoardRepository;
import com.jnu.capstone.repository.PostRepository;
import com.jnu.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LostBoardService {

    @Autowired
    private LostBoardRepository lostBoardRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public void createLostBoard(int userId, LostBoardCreateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

//        System.out.println("🔍 isLost 플래그: " + dto.isLost());
        // 1. Post 먼저 생성 및 저장
        Post post = new Post();
        post.setUser(user);
        post.setCampus(user.getCampus());
        post.setTitle(dto.getTitle());
        post.setContents(dto.getContents());
        post.setBoardType(BoardType.LOST);
        post.setIsDeleted(false);

        post = postRepository.save(post); // 반드시 저장하고 영속 상태로 받아야 함

        // 2. LostBoard 생성 후 post 연결
        LostBoard lostBoard = new LostBoard();
        lostBoard.setPost(post); // 영속 상태의 Post 연결
        lostBoard.setPlace(dto.getPlace());
        lostBoard.setWriteTime(new Date());
        lostBoard.setPhoto(dto.getPhoto());
        lostBoard.setLost(dto.isLost());
        lostBoard.setLostLatitude(dto.getLostLatitude());
        lostBoard.setLostLongitude(dto.getLostLongitude());

        // 3. LostBoard 저장
        lostBoardRepository.save(lostBoard);
    }

    public List<LostItemMapResponseDto> getFoundItemsForMap() {
        List<LostBoard> foundItems = lostBoardRepository.findByIsLostWithPost(false);  // false = 습득물

        return foundItems.stream()
                .map(item -> new LostItemMapResponseDto(
                        item.getPost().getPostId(),
                        item.getPost().getTitle(),
                        item.getLostLatitude(),
                        item.getLostLongitude()
                ))
                .collect(Collectors.toList());
    }

    public List<LostBoardDto> getLostBoardsByType(boolean isLost) {
        return lostBoardRepository.findByIsLostWithPost(isLost)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public LostBoardDto getLostBoardByPostId(int postId) {
        LostBoard board = lostBoardRepository.findWithPostByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToDto(board);
    }

    private LostBoardDto convertToDto(LostBoard board) {
        LostBoardDto dto = new LostBoardDto();
        dto.setPostId(board.getPostId());
        dto.setPlace(board.getPlace());
        dto.setContents(board.getPost().getContents()); // ✅ Post로부터 상세 설명 가져오기
        dto.setWriteTime(board.getWriteTime());
        dto.setPhoto(board.getPhoto());
        dto.setLost(board.isLost());
        dto.setLostLatitude(board.getLostLatitude());
        dto.setLostLongitude(board.getLostLongitude());
        dto.setRelativeTime(getRelativeTime(board.getWriteTime()));
        return dto;
    }

    private String getRelativeTime(Date writeTime) {
        long diffMillis = new Date().getTime() - writeTime.getTime();
        long minutes = diffMillis / (60 * 1000);
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";
        long hours = minutes / 60;
        if (hours < 24) return hours + "시간 전";
        long days = hours / 24;
        return days + "일 전";
    }

    // 자기 학교 것만 보이게 하는 거 .
    public List<LostBoardDto> getLostBoardsByUserCampusAndType(int userId, boolean isLost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        return lostBoardRepository.findByCampusAndIsLost(campusId, isLost).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 마커 자기 학교 것만 보이게 하는 거.
    public List<LostItemMapResponseDto> getFoundItemsForMapByUserCampus(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        List<LostBoard> foundItems = lostBoardRepository.findByCampusAndIsLost(campusId, false);

        return foundItems.stream()
                .filter(item -> item.getLostLatitude() != null && item.getLostLongitude() != null)
                .map(item -> new LostItemMapResponseDto(
                        item.getPost().getPostId(),
                        item.getPost().getTitle(),
                        item.getLostLatitude(),
                        item.getLostLongitude()
                ))
                .collect(Collectors.toList());
    }

    public List<LostBoardDto> searchBoardsByCampusAndType(int userId, String query, boolean isLost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        return lostBoardRepository.searchByCampusAndTextAndType(campusId, query, isLost).stream()
                .filter(item -> {
                    String title = item.getPost().getTitle().toLowerCase();
                    String contents = item.getPost().getContents().toLowerCase();
                    String keyword = query.toLowerCase();

                    // 제목 또는 내용에 정확히 "아이"가 포함된 경우만 통과
                    return title.contains(keyword) || contents.contains(keyword);
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



}
