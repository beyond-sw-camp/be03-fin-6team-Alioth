package com.alioth.statistics.domain.insurance.dto.res;

import lombok.Builder;

@Builder
public record InsuranceGodResDto(
        String memberCode,
        String memberName,
        String totalPrice,
        Long Count,
        String CancelAvgPrice,
        String performanceReview
) {


}
