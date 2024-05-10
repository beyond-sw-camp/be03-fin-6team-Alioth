package com.alioth.statistics.sales.dto.res;

import com.alioth.statistics.domain.batch.BatchTeamSales;
import lombok.Builder;

@Builder
public record SalesHQTotalTeamPriceResDto(
        String teamName,
        String contractPrice,
        String contractCount,
        String cancelPrice,
        String cancelCount
) {

}
