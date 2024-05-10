package com.alioth.statistics.sales.dto.res;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import lombok.Builder;

@Builder
public record SalesMemberMonthResDto(
        String contractPrice,
        String contractCount,
        String cancelPrice,
        String cancelCount
) {

    public static SalesMemberMonthResDto of(BatchMemberSales dto) {
        return SalesMemberMonthResDto.builder()
                .contractPrice(dto.getContractPrice())
                .contractCount(dto.getContractCount())
                .cancelPrice(dto.getCancelPrice())
                .cancelCount(dto.getCancelCount())
                .build();
    }

}
