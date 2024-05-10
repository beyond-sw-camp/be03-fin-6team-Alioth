package com.alioth.statistics.sales.dto.res;


import lombok.Builder;

@Builder
public record SalesTeamTotalPriceResDto(
        String contractPrice,
        String contractCount,
        String cancelPrice,
        String cancelCount
) {
}
