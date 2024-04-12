package com.alioth.statistics.service.insurance;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.insurance.dto.res.InsuranceGodResDto;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class InsuranceGodServiceImplTest {

    @Autowired
    private ContractRepository contractRepository;
    @Autowired private SalesMemberRepository salesMemberRepository;

    @Test
    @DisplayName("이번달 보험의 신")
    public void 이번달보험의신() {

        List<SalesMembers> memberList = salesMemberRepository.findAll();
        List<InsuranceGodResDto> dto = new LinkedList<>();

        for (var member : memberList) {
            LocalDateTime startDate = LocalDateTime.of(2024, 4, 2, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 5, 2, 0, 0);
            List<Contract> memberContractList = contractRepository.findBySalesMembersAndContractDateBetween(member, startDate, endDate);

            BigDecimal price = memberContractList.stream()
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            long count = memberContractList.stream().count();

            BigDecimal cancelTotalPrice = memberContractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            long cancelCount = memberContractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .count();

            String cancelAvgPrice = cancelTotalPrice.divide(BigDecimal.valueOf(cancelCount)).toPlainString();

            InsuranceGodResDto resDto = InsuranceGodResDto.builder()
                    .memberCode(member.getSalesMemberCode().toString())
                    .memberName(member.getName())
                    .totalPrice(price.toPlainString())
                    .Count(count)
                    .CancelAvgPrice(cancelAvgPrice)
                    .performanceReview(member.getPerformanceReview())
                    .build();

            dto.add(resDto);
        }

        dto.sort((o1, o2) -> {
            BigDecimal o2_divide = new BigDecimal(o2.totalPrice()).divide(BigDecimal.valueOf(o2.Count()));
            BigDecimal o2_cancelAvgPrice = new BigDecimal(o2.CancelAvgPrice());
            BigDecimal o2_subPrice = o2_divide.subtract(o2_cancelAvgPrice);

            BigDecimal o1_divide = new BigDecimal(o1.totalPrice()).divide(BigDecimal.valueOf(o1.Count()));
            BigDecimal o1_cancelAvgPrice = new BigDecimal(o1.CancelAvgPrice());
            BigDecimal o1_subPrice = o1_divide.subtract(o1_cancelAvgPrice);

            BigDecimal subtractResult = o2_subPrice.subtract(o1_subPrice);

            int result;
            if(subtractResult.compareTo(BigDecimal.ZERO) > 0) {
                result = 1;
            }else if(subtractResult.compareTo(BigDecimal.ZERO) < 0) {
                result = -1;
            }else {
                result = 0;
            }

            return result;
        });

        System.out.println("dto = " + dto);
        System.out.println("dto.get(0) = " + dto.get(0));

    }

    @Test
    @DisplayName("반기별 보험의 신")
    @Transactional
    public void 반기별보험의신() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        LocalDateTime startDate = LocalDateTime.of(2024, 4, 2, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 2, 0, 0);

        for (var member : memberList) {
            List<Contract> bySalesMembersAndContractDate = contractRepository.findBySalesMembersAndContractDateBetween(member, startDate, endDate);


            System.out.println("bySalesMembersAndContractDate = " + bySalesMembersAndContractDate);
        }




    }

    @Test
    @DisplayName("올해의 보험의 신")
    public void 올해의보험의신() {

    }

}