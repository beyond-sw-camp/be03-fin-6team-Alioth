package com.alioth.statistics.domain.batch.dto.res;

import com.alioth.statistics.domain.batch.BatchTeamSales;
import lombok.Builder;
import lombok.Setter;

@Builder
public record BatchTeamSalesResDto(
    String teamName,
    String teamCode,
    String contractPrice,
    String contractCount,
    String cancelPrice,
    String cancelCount
) {

    public static BatchTeamSalesResDto ofBatchTeamSales(BatchTeamSales team) {
        return BatchTeamSalesResDto.builder()
                .teamName(team.getTeamName())
                .teamCode(String.valueOf(team.getTeamCode()))
                .contractPrice(team.getContractPrice())
                .contractCount(team.getContractCount())
                .cancelPrice(team.getCancelPrice())
                .cancelCount(team.getCancelCount())
                .build();
    }

}
