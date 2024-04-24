package com.alioth.server.domain.login.dto.res;

import lombok.Builder;

@Builder
public record LoginResDto(
    Long memberCode,
    String memberRank,
    String memberTeam,
    String name,
    String email,
    String image,
    String accessToken,
    String refreshToken
) {

}
