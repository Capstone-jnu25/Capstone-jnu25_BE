package com.jnu.capstone.service;

import com.jnu.capstone.dto.ApplicantResponseDto;
import com.jnu.capstone.dto.ApplicantSimpleResponseDto;
import com.jnu.capstone.entity.Applicant;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.repository.ApplicantRepository;
import com.jnu.capstone.repository.GatheringBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private GatheringBoardRepository gatheringBoardRepository;

    public Page<?> getApplicants(int postId, Pageable pageable) {
        // 게시글 찾기
        GatheringBoard gatheringBoard = gatheringBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Post post = gatheringBoard.getPost();
        BoardType boardType = gatheringBoard.getBoardType();

        // 스터디 게시글
        if (boardType == BoardType.STUDY) {
            return applicantRepository.findByPost_PostIdAndPost_BoardType(postId, boardType, pageable)
                    .map(ApplicantResponseDto::fromEntity);
        }

        // 번개 게시글
        else if (boardType == BoardType.MEETUP) {
            return applicantRepository.findByPost_PostIdAndPost_BoardType(postId, boardType, pageable)
                    .map(ApplicantSimpleResponseDto::fromEntity);
        }

        // 그 외 게시판 (예외 처리)
        else {
            throw new IllegalArgumentException("지원자 목록은 스터디와 번개 모임 게시판에서만 조회할 수 있습니다.");
        }
    }
}
