package com.alioth.server.domain.team.dto;

import com.alioth.server.domain.member.dto.res.SMTeamListResDto;
import lombok.Builder;

import java.util.List;

@Builder
public record TeamResDto(
  String teamName,
  String teamCode,
  Long teamManagerCode,
  List<SMTeamListResDto> teamMemberList

) {}