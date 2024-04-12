package com.alioth.statistics.common.batch.tasklet;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import com.alioth.statistics.domain.batch.repository.BatchMemberSalesRepository;
import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.dummy.domain.InsuranceProduct;
import com.alioth.statistics.domain.dummy.domain.InsuranceProductCategory;
import com.alioth.statistics.domain.dummy.repository.InsuranceProductRepository;
import com.alioth.statistics.domain.insurance.dto.res.InsuranceGodResDto;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MemberSalesTasklet {

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;
    private final BatchMemberSalesRepository batchMemberSalesRepository;

    @Bean(name = "taskletMemberSales")
    public Tasklet taskletMemberSales(){
        // 년, 월, 시, 일, 분
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute, 0, 0);

        return ((contribution, chunkContext) -> {
            log.info("================================================");
            log.info("===========This is taskletMemberSales===========");

            List<SalesMembers> memberList = salesMemberRepository.findAll();
            LocalDateTime endDate = LocalDateTime.of(year, month, day, 0, 0);
            LocalDateTime startDate = endDate.minusDays(1L);
            List<BatchMemberSales> batchMemberSalesList = new LinkedList<>();

            for (var member : memberList) {
                List<Contract> contractList = contractRepository.findBySalesMembersAndContractDateBetween(member, startDate, endDate);

                BigDecimal totalPrice = contractList.stream()
                        .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Long totalCount = contractList.stream().count();

                BigDecimal cancelPrice = contractList.stream()
                        .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                        .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                long cancelCount = contractList.stream()
                        .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                        .count();

                BatchMemberSales createMember = BatchMemberSales.builder()
                        .salesMemberCode(member.getSalesMemberCode())
                        .salesMemberName(member.getName())
                        .contractPrice(totalPrice.toPlainString())
                        .contractCount(String.valueOf(totalCount))
                        .cancelPrice(cancelPrice.toPlainString())
                        .cancelCount(String.valueOf(cancelCount))
                        .createdTime(time)
                        .build();

                batchMemberSalesList.add(createMember);
            }

            batchMemberSalesRepository.saveAll(batchMemberSalesList);

            log.info("===========This is taskletMemberSales===========");
            log.info("================================================");

            return RepeatStatus.FINISHED;
        });
    }

}
