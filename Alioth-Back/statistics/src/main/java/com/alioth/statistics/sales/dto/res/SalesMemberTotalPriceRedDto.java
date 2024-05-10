package com.alioth.statistics.sales.dto.res;


import lombok.Builder;

@Builder
public record SalesMemberTotalPriceRedDto(
        String contractPrice,
        String contractCount,
        String cancelPrice,
        String cancelCount
) {
}
