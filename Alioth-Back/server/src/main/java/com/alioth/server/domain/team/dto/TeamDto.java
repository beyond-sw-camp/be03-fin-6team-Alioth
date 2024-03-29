package com.alioth.server.domain.team.dto;

import com.alioth.server.domain.member.dto.res.SalesMemberTeamListResDto;
import lombok.Builder;

import java.util.List;

@Builder
public record TeamDto (
    String teamCode,
    String teamName,
    Long teamManagerCode,
    List<SalesMemberTeamListResDto> teamMemberList

){}
