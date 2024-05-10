package com.alioth.statistics.sales.dto.res;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import com.alioth.statistics.domain.batch.BatchRankMember;
import com.alioth.statistics.domain.batch.dto.res.BatchMemberSalesResDto;
import lombok.Builder;

@Builder
public record SalesMemberRankResDto(
        String salesMemberName,
        String salesMemberCode,
        String contractPrice,
        String contractCount
) {
    public static SalesMemberRankResDto ofSalesMemberRankResDto(BatchRankMember member) {
        return SalesMemberRankResDto.builder()
                .salesMemberName(member.getMemberName())
                .salesMemberCode(String.valueOf(member.getMemberCode()))
                .contractPrice(member.getContractPrice())
                .contractCount(member.getContractCount())
                .build();
    }
}
