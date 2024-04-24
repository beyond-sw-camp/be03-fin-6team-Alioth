package com.alioth.statistics.domain.batch.dto.res;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import lombok.Builder;

@Builder
public record BatchMemberSalesResDto(
    String salesMemberName,
    String salesMemberCode,
    String contractPrice,
    String contractCount,
    String cancelPrice,
    String cancelCount
) {

    public static BatchMemberSalesResDto ofBatchMemberSales(BatchMemberSales member) {
        return BatchMemberSalesResDto.builder()
                .salesMemberName(member.getSalesMemberName())
                .salesMemberCode(String.valueOf(member.getSalesMemberCode()))
                .contractPrice(member.getContractPrice())
                .contractCount(member.getContractCount())
                .cancelPrice(member.getCancelPrice())
                .cancelCount(member.getCancelCount())
                .build();
    }

}
