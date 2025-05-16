package com.jnu.capstone.service;

import java.time.LocalDate;
import com.jnu.capstone.dto.PostResponseDto;
import com.jnu.capstone.dto.GatheringDetailResponseDto;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.repository.GatheringBoardRepository;
import com.jnu.capstone.repository.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GatheringService {

    @Autowired
    private GatheringBoardRepository gatheringBoardRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    public Page<PostResponseDto> getGatheringPosts(String boardType, Pageable pageable) {
        // String -> Enum 변환
        BoardType type = BoardType.valueOf(boardType.toUpperCase());

        // 게시글 목록 조회
        return gatheringBoardRepository.findByBoardType(type, pageable)
                .map(gatheringBoard -> {
                    LocalDate dueDate = gatheringBoard.getDueDate();
                    long dDay = Math.max(0, dueDate.toEpochDay() - LocalDate.now().toEpochDay());

                    // 마감 조건
                    boolean isClosed = false;

                    // 1. 마감일이 지난 경우
                    if (dueDate.isBefore(LocalDate.now())) {
                        isClosed = true;
                    }

                    // 2. 자동 수락 (automatic = true)
                    else if (gatheringBoard.isAutomatic() && gatheringBoard.getCurrentParticipants() >= gatheringBoard.getMaxParticipants()) {
                        isClosed = true;
                    }

                    // 3. 수동 수락 (automatic = false)
                    else if (!gatheringBoard.isAutomatic()) {
                        int acceptedCount = (int) applicantRepository.countByPostAndIsAccepted(gatheringBoard.getPost(), true);
                        if (acceptedCount >= gatheringBoard.getMaxParticipants()) {
                            isClosed = true;
                        }
                    }

                    return new PostResponseDto(
                            gatheringBoard.getPost().getPostId(),
                            gatheringBoard.getPost().getTitle(),
                            gatheringBoard.getPost().getContents(),
                            gatheringBoard.getPlace(),
                            gatheringBoard.getMeetTime(),
                            dueDate,
                            gatheringBoard.getGender().toString(),
                            gatheringBoard.getMaxParticipants(),
                            gatheringBoard.getCurrentParticipants(),
                            gatheringBoard.getBoardType().toString(),
                            isClosed,
                            dDay
                    );
                });
    }

    public GatheringDetailResponseDto getGatheringDetail(int postId) {
        GatheringBoard gatheringBoard = gatheringBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        long dDay = Math.max(0, LocalDate.now().until(gatheringBoard.getDueDate()).getDays());
        String dDayText = dDay == 0 ? "D-DAY" : "D-" + dDay;

        boolean isClosed = gatheringBoard.getDueDate().isBefore(LocalDate.now()) ||
                (gatheringBoard.isAutomatic() && gatheringBoard.getMaxParticipants() <= gatheringBoard.getCurrentParticipants());

        return new GatheringDetailResponseDto(
                gatheringBoard.getPost().getPostId(),
                gatheringBoard.getPost().getTitle(),
                dDayText,
                gatheringBoard.getPost().getContents(),
                gatheringBoard.getPlace(),
                gatheringBoard.getGender().toString(),
                gatheringBoard.getMeetTime(),
                isClosed
        );
    }
}

