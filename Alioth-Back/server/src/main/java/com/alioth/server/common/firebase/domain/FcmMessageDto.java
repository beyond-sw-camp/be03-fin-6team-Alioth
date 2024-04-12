package com.alioth.server.common.firebase.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class FcmMessageDto {
    private boolean validateOnly;
    private FcmMessageDto.Message message;

    @Builder
    @AllArgsConstructor    @Getter    public static class Message {
        private FcmMessageDto.Notification notification;
        private String token;
        private Map<String, String> data;
    }

    @Builder
    @AllArgsConstructor    @Getter    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}