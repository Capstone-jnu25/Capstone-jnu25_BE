package com.jnu.capstone.service;

import com.jnu.capstone.dto.ApplicantResponseDto;
import com.jnu.capstone.dto.ApplicantSimpleResponseDto;
import com.jnu.capstone.entity.Applicant;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.ChatJoin;
import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.ApplicantRepository;
import com.jnu.capstone.repository.GatheringBoardRepository;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.ChatJoinRepository;
import com.jnu.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicantService {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private GatheringBoardRepository gatheringBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatJoinRepository chatJoinRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Transactional(readOnly = true)
    public Page<?> getApplicants(int postId, Pageable pageable) {
        // 게시글 찾기
        GatheringBoard gatheringBoard = gatheringBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Post post = gatheringBoard.getPost();
        BoardType boardType = gatheringBoard.getBoardType();

        // 스터디 게시글
        if (boardType == BoardType.STUDY) {
            return applicantRepository.findByPost_PostIdAndPost_BoardType(postId, boardType, pageable)
                    .map(applicant -> {
                        // User 엔티티를 초기화하여 LazyInitializationException 방지
                        applicant.getUser().getNickname();
                        return ApplicantResponseDto.fromEntity(applicant);
                    });
        } else if (boardType == BoardType.MEETUP) {
            return applicantRepository.findByPost_PostIdAndPost_BoardType(postId, boardType, pageable)
                    .map(applicant -> {
                        // User 엔티티를 초기화하여 LazyInitializationException 방지
                        applicant.getUser().getNickname();
                        return ApplicantSimpleResponseDto.fromEntity(applicant);
                    });
        } else {
            throw new IllegalArgumentException("지원자 목록은 스터디와 번개 모임 게시판에서만 조회할 수 있습니다.");
        }
    }

    @Transactional
    public void applyForGathering(int postId, int userId, String applicationText) {
        // 게시글 찾기
        GatheringBoard gatheringBoard = gatheringBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 이미 지원했는지 확인
        boolean alreadyApplied = applicantRepository.existsByPostAndUser(gatheringBoard.getPost(), user);
        if (alreadyApplied) {
            throw new IllegalArgumentException("이미 지원한 게시글입니다.");
        }

        // 모집이 마감되었는지 확인
        if (gatheringBoard.getCurrentParticipants() >= gatheringBoard.getMaxParticipants()) {
            throw new IllegalArgumentException("모집이 마감된 게시글입니다.");
        }

        // 지원자 객체 생성
        Applicant applicant = new Applicant();
        applicant.setPost(gatheringBoard.getPost());
        applicant.setUser(user);

        // 스터디인 경우 지원 문구 저장
        if (gatheringBoard.getBoardType() == BoardType.STUDY) {
            applicant.setApplicationText(applicationText);
        }

        // 자동 수락 처리
        if (gatheringBoard.isAutomatic()) {
            applicant.setAccepted(true);
            gatheringBoard.setCurrentParticipants(gatheringBoard.getCurrentParticipants() + 1);
        }

        // DB에 저장
        applicantRepository.save(applicant);
        gatheringBoardRepository.save(gatheringBoard);
    }

    @Transactional
    public void acceptApplicant(int postId, int applicantId, int userId) {
        // 지원자 찾기
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지원자가 존재하지 않습니다."));

        // 게시글 확인
        Post post = applicant.getPost();
        if (post.getPostId() != postId) {
            throw new IllegalArgumentException("지원자가 해당 게시글의 신청자가 아닙니다.");
        }

        // 게시글 작성자 확인 (User-Id 헤더)
        if (post.getUser().getUserId() != userId) {
            throw new IllegalArgumentException("게시글 작성자만 지원자를 수락할 수 있습니다.");
        }

        // 이미 수락된 지원자인지 확인
        if (applicant.isAccepted()) {
            throw new IllegalArgumentException("해당 지원자는 이미 수락되었습니다.");
        }

        // 지원자 수락 처리
        applicant.setAccepted(true);
        applicantRepository.save(applicant);

        // 채팅방 ID 찾기
        Chatroom chatroom = chatroomRepository.findByPost_PostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글에 대한 채팅방이 존재하지 않습니다."));

        // chat_join 테이블에 추가
        ChatJoin chatJoin = new ChatJoin();
        chatJoin.setUser_id(applicant.getUser().getUserId());
        chatJoin.setChatting_room_id(chatroom.getChattingRoomId());
        chatJoinRepository.save(chatJoin);

        GatheringBoard gatheringBoard = post.getGatheringBoard();
        gatheringBoard.setCurrentParticipants(gatheringBoard.getCurrentParticipants() + 1);
    }

    @Transactional
    public void deleteApplicant(int postId, int applicantId, int userId) {
        // 지원자 찾기
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지원자가 존재하지 않습니다."));

        // 게시글 확인
        Post post = applicant.getPost();
        if (post.getPostId() != postId) {
            throw new IllegalArgumentException("지원자가 해당 게시글의 신청자가 아닙니다.");
        }

        // 게시글 작성자 확인 (User-Id 헤더)
        if (post.getUser().getUserId() != userId) {
            throw new IllegalArgumentException("게시글 작성자만 지원자를 삭제할 수 있습니다.");
        }

        // 지원자 삭제
        applicantRepository.delete(applicant);
    }
}
