package com.alioth.statistics.service.cencellation;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class SMCancellationServiceTest {

    @Autowired private ContractRepository contractRepository;

    @Autowired private SalesMemberRepository salesMemberRepository;

    private ContractStatus contractStatus = ContractStatus.Cancellation;


    @Test
    @Transactional
    public void 개인해약율() {

        Map<SalesMembers, String> result = new LinkedHashMap<>();
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        for (var member : memberList) {

            List<Contract> contractList = contractRepository.findBySalesMembers(member);
            BigDecimal totalPrice = contractList.stream()
                    .map(x -> new BigDecimal(x.getContractTotalPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ONE);

            BigDecimal cancelPrice = contractList.stream()
                    .filter(x -> x.getContractStatus() == ContractStatus.Cancellation)
                    .map(x -> new BigDecimal(x.getContractTotalPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ONE);

            if(cancelPrice.equals(BigDecimal.ZERO)) {
                result.put(member, "0%");
            }

            BigDecimal percent = new BigDecimal("100");
            BigDecimal divide = cancelPrice.divide(totalPrice, 3, RoundingMode.HALF_EVEN);
            BigDecimal multiply = divide.multiply(percent);

            System.out.println("divide = " + multiply);
            result.put(member, multiply+"%");
        }

        System.out.println("result = " + result);
    }



    @Test
    @Transactional
    public void 개인해약건() {

        Map<SalesMembers, String> result = new LinkedHashMap<>();
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        for (var member : memberList) {
            List<Contract> contractList = contractRepository.findBySalesMembers(member);
            long contractSize = contractList.stream()
                    .count();

            long cancelSize = contractList.stream()
                    .filter(x -> x.getContractStatus() == ContractStatus.Cancellation)
                    .count();

            if(cancelSize == 0L) {
                result.put(member, "0%");
            }

            double v = ((double)cancelSize / (double)contractSize) * 100;
            String strResult = String.format("%.3f", v);
            result.put(member, strResult);
        }

        System.out.println("result = " + result);
    }

}