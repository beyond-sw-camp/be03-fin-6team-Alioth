package com.alioth.statistics.sales.dto.res;

public record SalesPersonalListResDto(
    String customer,
    String contractPeriod,
    String contractDate,
    String expirationTime,
    String contractState

) {
}
