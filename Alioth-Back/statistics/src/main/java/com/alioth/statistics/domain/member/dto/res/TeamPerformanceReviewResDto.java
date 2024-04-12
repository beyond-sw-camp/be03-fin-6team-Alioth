package com.alioth.statistics.domain.member.dto.res;

import lombok.Builder;

@Builder
public record TeamPerformanceReviewResDto(

    String teamCode,
    String teamPerformanceReview,
    String teamTotal,
    String teamCount

) {
}
