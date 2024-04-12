package com.alioth.statistics.domain.member.dto.res;

import lombok.Builder;

@Builder
public record MemberPerformanceReviewResDto(
    String salesMemberCode,
    String name,
    String performanceReview,
    String totalCount,
    String totalPrice
) {



}
