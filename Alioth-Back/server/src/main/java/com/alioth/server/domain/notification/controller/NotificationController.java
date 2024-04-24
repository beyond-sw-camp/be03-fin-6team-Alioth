package com.alioth.server.domain.notification.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.notification.domain.Notification;
import com.alioth.server.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{salesMemberId}")
    public ResponseEntity<CommonResponse> getNotifications(@PathVariable Long salesMemberId) {
        List<Notification> notifications = notificationService.getNotificationsForSalesMember(salesMemberId);
        return CommonResponse.responseMessage(HttpStatus.OK, "알림 리스트 조회 성공", notifications);

    }
}
