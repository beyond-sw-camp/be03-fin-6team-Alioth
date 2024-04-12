package com.alioth.statistics.service.performance;


import com.alioth.statistics.domain.member.dto.res.MemberPerformanceReviewResDto;
import com.alioth.statistics.domain.member.dto.res.TeamPerformanceReviewResDto;

import java.util.List;

public interface PerformanceReviewService {

    List<MemberPerformanceReviewResDto> performanceReviewMember();
    List<TeamPerformanceReviewResDto> performanceReviewTeam();

}
