package com.alioth.server.domain.notification.service;



import com.alioth.server.domain.notification.domain.Notification;
import com.alioth.server.domain.notification.domain.ReadStatus;
import com.alioth.server.domain.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> getNotificationsForSalesMember(Long salesMemberId) {
        return notificationRepository.findAllBySalesMemberIdAndReadStatus(salesMemberId, ReadStatus.Unread);
    }
}