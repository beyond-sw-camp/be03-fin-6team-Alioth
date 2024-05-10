package com.alioth.statistics.dashboard.dto.res;

import lombok.Builder;

@Builder
public record DashboardBestTeamResDto(
        String teamName,
        String price,
        String count
) {
}
