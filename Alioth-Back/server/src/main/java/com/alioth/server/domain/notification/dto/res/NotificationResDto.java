package com.alioth.server.domain.notification.dto.res;


import com.alioth.server.domain.notification.domain.ReadStatus;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public class NotificationResDto {
    private Long id;
    private Long salesMemberId;
    private String title;
    private String message;
    private ReadStatus readStatus;
    private LocalDateTime createdAt;
}