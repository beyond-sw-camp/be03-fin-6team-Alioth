package com.alioth.statistics.common.batch.tasklet;

import com.alioth.statistics.domain.batch.BatchRankMember;
import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RankMemberTaskletTest {

    @Autowired private ContractRepository contractRepository;
    @Autowired private SalesMemberRepository salesMemberRepository;

    @Test
    @DisplayName("사원 랭킹 통계")
    public void 사원랭킹통계() {

        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonthValue();

        LocalDateTime endDate = LocalDateTime.of(year, month, 3, 0, 0);
        LocalDateTime startDate = endDate.minusDays(1L);

        List<Contract> contractList = contractRepository.findByContractDateBetween(startDate, endDate);

        Map<Long, List<Contract>> memberContract = contractList.stream()
                .collect(Collectors.groupingBy(contract -> contract.getSalesMembers().getSalesMemberCode()));

        System.out.println("collect = " + memberContract);

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


        System.out.println("rankMemberList = " + rankMemberList);


    }
}