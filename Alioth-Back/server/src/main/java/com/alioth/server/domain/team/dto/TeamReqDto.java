package com.alioth.server.domain.team.dto;

import lombok.Builder;


@Builder
public record TeamReqDto(
    String teamName,
    Long teamManagerCode


){}
