package com.alioth.server.domain.team.dto;

import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import lombok.Builder;

import java.util.List;

@Builder
public record TeamResDto(
  String teamName,
  String teamCode,
  String teamManagerName,
  String performanceReview,
  List<SalesMemberResDto> teamMemberList

) {}