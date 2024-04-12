package com.alioth.statistics.common.batch.tasklet;

import com.alioth.statistics.domain.batch.BatchRankMember;
import com.alioth.statistics.domain.batch.repository.BatchRankMemberRepository;
import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RankMemberTasklet {

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;
    private final BatchRankMemberRepository batchRankMemberRepository;

    @Bean(name = "taskletRankMember")
    public Tasklet taskletRankMember(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute, 0 , 0);


        return ((contribution, chunkContext) -> {
            log.info("===============================================");
            log.info("===========This is taskletRankMember===========");

            LocalDateTime endDate = LocalDateTime.of(year, month, 3, 0, 0);
            LocalDateTime startDate = endDate.minusDays(1L);
            List<Contract> contractList = contractRepository.findByContractDateBetween(startDate, endDate);

            Map<Long, List<Contract>> memberContract = contractList.stream()
                    .collect(Collectors.groupingBy(contract -> contract.getSalesMembers().getSalesMemberCode()));

            List<BatchRankMember> rankMemberList = new LinkedList<>();
            for (var memberCode : memberContract.keySet()) {
                SalesMembers findMember = salesMemberRepository.findBySalesMemberCode(memberCode).orElseThrow(() -> new EntityNotFoundException("계정 정보를 찾을 수 없습니다."));

                BigDecimal totalPrice = memberContract.get(memberCode).stream()
                        .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                int contractCount = memberContract.get(memberCode).size();

                BatchRankMember rankMember = BatchRankMember.builder()
                        .memberCode(memberCode)
                        .memberName(findMember.getName())
                        .memberRank(-1L)
                        .contractPrice(totalPrice.toPlainString())
                        .contractCount(String.valueOf(contractCount))
                        .createdDate(LocalDateTime.now())
                        .build();

                rankMemberList.add(rankMember);
            }

            rankMemberList.sort((o1, o2) -> {
                BigDecimal o2Price = new BigDecimal(o2.getContractPrice());
                BigDecimal o1Price = new BigDecimal(o1.getContractPrice());
                return o2Price.compareTo(o1Price);
            });

            Long rank = 1L;
            for (var rankMember : rankMemberList) {
                rankMember.updateMemberRank(rank++);
            }

            batchRankMemberRepository.saveAll(rankMemberList);

            log.info("===========This is taskletRankMember===========");
            log.info("===============================================");

            return RepeatStatus.FINISHED;
        });
    }
}
