package com.alioth.statistics.sales.dto.res;

import lombok.Builder;

@Builder
public record SalesTeamTargetResDto(
        Long targetPrice,
        Long price
) {
}
