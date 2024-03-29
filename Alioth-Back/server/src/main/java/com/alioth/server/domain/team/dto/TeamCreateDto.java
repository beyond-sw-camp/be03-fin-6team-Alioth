package com.alioth.server.domain.team.dto;

import lombok.Builder;

@Builder
public record TeamCreateDto(
  String teamName,
  Long teamManagerCode

) {}