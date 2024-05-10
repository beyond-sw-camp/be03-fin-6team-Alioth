package com.alioth.statistics.sales.dto.res;

import lombok.Builder;

@Builder
public record SalesMemberTargetResDto(
        Long target,
        Long price
) {
}
