package com.alioth.statistics.dashboard.dto.res;

import lombok.Builder;

@Builder
public record DashboardGodResDto(
        String name,
        String price,
        String count
) {
}
