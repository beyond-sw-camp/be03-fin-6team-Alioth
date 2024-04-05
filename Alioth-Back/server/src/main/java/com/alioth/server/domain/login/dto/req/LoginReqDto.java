package com.alioth.server.domain.login.dto.req;

import lombok.Builder;

@Builder
public record LoginReqDto(
    Long memberCode,
    String password,
    String fcmToken
) {
}
