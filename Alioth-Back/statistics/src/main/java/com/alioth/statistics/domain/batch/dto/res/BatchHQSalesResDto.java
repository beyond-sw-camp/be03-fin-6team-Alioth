package com.alioth.statistics.domain.batch.dto.res;

import com.alioth.statistics.domain.batch.BatchHQSales;
import lombok.Builder;

@Builder
public record BatchHQSalesResDto(
    String contractPrice,
    String contractCount,
    String cancelPrice,
    String cancelCount
) {

    public static BatchHQSalesResDto ofBatchHQSales(BatchHQSales hq) {
        return BatchHQSalesResDto.builder()
                .contractPrice(hq.getTotalPrice())
                .contractCount(hq.getTotalCount())
                .cancelPrice(hq.getCancelPrice())
                .cancelCount(hq.getCancelCount())
                .build();
    }


}
