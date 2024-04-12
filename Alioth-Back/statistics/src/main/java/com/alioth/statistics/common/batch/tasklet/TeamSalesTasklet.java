package com.alioth.statistics.common.batch.tasklet;

import com.alioth.statistics.domain.batch.BatchTeamSales;
import com.alioth.statistics.domain.batch.repository.BatchTeamSalesRepository;
import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TeamSalesTasklet {

    private final ContractRepository contractRepository;
    private final BatchTeamSalesRepository batchTeamSalesRepository;


    @Bean(name = "taskletTeamSales")
    public Tasklet taskletTeamSales(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute, 0, 0);

        return ((contribution, chunkContext) -> {

            log.info("==============================================");
            log.info("===========This is taskletTeamSales===========");
            LocalDateTime endDate = LocalDateTime.of(year, month, 3, 0, 0);
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
                        .createdTime(time)
                        .build();

                batchTeamSalesList.add(teamSales);
            }

            batchTeamSalesList.sort((o1, o2) -> {
                BigDecimal o2Price = new BigDecimal(o2.getContractPrice());
                BigDecimal o1Price = new BigDecimal(o1.getContractPrice());
                return o2Price.compareTo(o1Price);
            });

            batchTeamSalesRepository.saveAll(batchTeamSalesList);

            log.info("===========This is taskletTeamSales===========");
            log.info("==============================================");

            return RepeatStatus.FINISHED;
        });
    }
}
