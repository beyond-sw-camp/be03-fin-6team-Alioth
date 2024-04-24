package com.alioth.server.domain.notification.repository;

import com.alioth.server.domain.notification.domain.Notification;
import com.alioth.server.domain.notification.domain.ReadStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllBySalesMemberIdAndReadStatus(Long salesMemberId, ReadStatus readStatus);
    boolean existsByMessageId(String messageId);
}