package com.jnu.capstone.repository;
import com.jnu.capstone.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
//    List<NotificationLog> findByUser_UserIdOrderBySentTimeDesc(int userId);
    List<NotificationLog> findByUser_UserId(int userId);
}
