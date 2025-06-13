package com.jnu.capstone.scheduler;

import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.repository.GatheringBoardRepository;
import com.jnu.capstone.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostCleanupScheduler {

    private final GatheringBoardRepository gatheringBoardRepository;
    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    public void deleteExpiredGatheringPosts() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);
        List<BoardType> targetTypes = List.of(BoardType.MEETUP, BoardType.STUDY);

        List<GatheringBoard> expiredBoards = gatheringBoardRepository
                .findByBoardTypeInAndDueDateBefore(targetTypes, cutoffDate)
                .stream()
                .filter(board -> !board.getPost().isDeleted()) // 아직 삭제되지 않은 게시글만
                .collect(Collectors.toList());

        log.info("[스케줄러 실행] 만료된 게시글 논리 삭제 처리 중...", expiredBoards.size());

        for (GatheringBoard board : expiredBoards) {
            Post post = board.getPost();
            post.setIsDeleted(true); // ✅ 실제 삭제 대신 논리 삭제
            postRepository.save(post);
        }
        log.info("[스케줄러 완료] 논리 삭제 처리 완료.");
    }


}
