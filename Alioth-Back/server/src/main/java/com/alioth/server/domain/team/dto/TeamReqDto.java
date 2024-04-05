package com.alioth.server.domain.team.dto;

import lombok.Builder;


@Builder
public record TeamReqDto(
    String teamCode,
    String teamName,
    Long teamManagerCode


){}
