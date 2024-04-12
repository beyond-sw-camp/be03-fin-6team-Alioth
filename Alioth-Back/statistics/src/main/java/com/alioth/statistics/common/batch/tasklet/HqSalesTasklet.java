package com.alioth.statistics.common.batch.tasklet;

import com.alioth.statistics.domain.batch.BatchHQSales;
import com.alioth.statistics.domain.batch.repository.BatchHQSalesRepository;
import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HqSalesTasklet {

    private final BatchHQSalesRepository batchHQSalesRepository;
    private final ContractRepository contractRepository;

    @Bean(name = "taskletHqSales")
    public Tasklet taskletHqSales(){

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute, 0, 0);

        return ((contribution, chunkContext) -> {
            log.info("============================================");
            log.info("===========This is TaskletHqSales===========");

            LocalDateTime endDate = LocalDateTime.of(year, month, 3, 0, 0);
            LocalDateTime startDate = endDate.minusDays(1L);

            Map<SalesMembers, List<Contract>> memberCollect = contractRepository.findByContractDateBetween(startDate, endDate).stream()
                    .collect(Collectors.groupingBy(contract -> contract.getSalesMembers()));

            List<Contract> contractList = memberCollect.values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            BigDecimal totalPrice = contractList.stream()
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Long totalCount = contractList.stream().count();

            BigDecimal cancellationTotalPrice = contractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Long cancellationCount = contractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .count();

            BatchHQSales hqSales = BatchHQSales.builder()
                    .totalPrice(totalPrice.toPlainString())
                    .totalCount(String.valueOf(totalCount))
                    .cancelPrice(cancellationTotalPrice.toPlainString())
                    .cancelCount(String.valueOf(cancellationCount))
                    .createdDate(time)
                    .build();

            batchHQSalesRepository.save(hqSales);

            log.info("===========This is TaskletHqSales===========");
            log.info("============================================");
            return RepeatStatus.FINISHED;
        });
    }
}
