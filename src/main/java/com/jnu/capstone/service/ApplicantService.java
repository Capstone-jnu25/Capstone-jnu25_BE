package com.jnu.capstone.service;

import com.jnu.capstone.dto.ApplicantResponseDto;
import com.jnu.capstone.dto.ApplicantSimpleResponseDto;
import com.jnu.capstone.entity.Applicant;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.ApplicantRepository;
import com.jnu.capstone.repository.GatheringBoardRepository;
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

        // 지원자 객체 생성
        Applicant applicant = new Applicant();
        applicant.setPost(gatheringBoard.getPost());
        applicant.setUser(user);

        // 스터디인 경우 지원 문구 저장
        if (gatheringBoard.getBoardType() == BoardType.STUDY) {
            applicant.setApplicationText(applicationText);
        }

        // DB에 저장
        applicantRepository.save(applicant);
    }
}
