package com.alioth.statistics.sales.dto.res;

public record SalesPerformanceResDto(
    String totalPrice,
    String target,
    String count,
    String newMemberCount
) {
}
