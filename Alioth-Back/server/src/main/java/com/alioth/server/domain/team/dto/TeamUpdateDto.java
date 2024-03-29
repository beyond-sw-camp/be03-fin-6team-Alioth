package com.alioth.server.domain.team.dto;

import lombok.Builder;

@Builder
public record TeamUpdateDto(

    String teamName,
    Long teamManagerCode

){}
