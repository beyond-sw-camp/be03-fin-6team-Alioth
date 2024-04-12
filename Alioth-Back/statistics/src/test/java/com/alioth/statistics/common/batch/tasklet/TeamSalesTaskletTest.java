package com.alioth.statistics.common.batch.tasklet;

import com.alioth.statistics.domain.batch.BatchTeamSales;
import com.alioth.statistics.domain.batch.repository.BatchTeamSalesRepository;
import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TeamSalesTaskletTest {

    @Autowired private ContractRepository contractRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private BatchTeamSalesRepository batchTeamSalesRepository;

    @Test
    @DisplayName("배치 팀 결산 테스트")
    public void 배치팀결산테스트() {

        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth().getValue(), 3, 0, 0);
        LocalDateTime startDate = endDate.minusDays(1L);
        List<Contract> contractList = contractRepository.findByContractDateBetween(startDate, endDate);

        Map<Team, List<Contract>> collect = contractList.stream()
                .collect(Collectors.groupingBy(e -> e.getSalesMembers().getTeam()));

        List<BatchTeamSales> batchTeamSalesList = new LinkedList<>();
        for (var team : collect.keySet()) {
            List<Contract> teamContractList = collect.get(team);

            BigDecimal teamPrice = teamContractList.stream()
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Long teamCount = teamContractList.stream().count();

            BigDecimal teamCancellationPrice = teamContractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Long teamCancellationCount = teamContractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .count();

            BatchTeamSales teamSales = BatchTeamSales.builder()
                    .teamCode(team.getTeamCode())
                    .teamName(team.getTeamName())
                    .contractPrice(teamPrice.toPlainString())
                    .contractCount(String.valueOf(teamCount))
                    .cancelPrice(teamCancellationPrice.toPlainString())
                    .cancelCount(String.valueOf(teamCancellationCount))
                    .createdTime(null)
                    .build();

            batchTeamSalesList.add(teamSales);
        }

        batchTeamSalesList.sort((o1, o2) -> {
            BigDecimal o2Price = new BigDecimal(o2.getContractPrice());
            BigDecimal o1Price = new BigDecimal(o1.getContractPrice());
            return o2Price.compareTo(o1Price);
        });

        System.out.println("batchTeamSalesList = " + batchTeamSalesList);

    }

}