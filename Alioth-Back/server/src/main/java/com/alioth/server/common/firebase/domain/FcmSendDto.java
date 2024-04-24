package com.alioth.server.common.firebase.domain;

import lombok.Builder;

public record FcmSendDto(String token, String title, String body, String url, String messageId) {

    @Builder
    public FcmSendDto {}
}
