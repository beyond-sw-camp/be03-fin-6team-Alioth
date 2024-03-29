package com.alioth.server.domain.login.dto.res;

import lombok.Builder;

@Builder
public record LoginResDto(
    Long memberCode,
    String name,
    String accessToken,
    String refreshToken
) {

}
