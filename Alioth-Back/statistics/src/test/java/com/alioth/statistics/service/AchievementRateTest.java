package com.alioth.statistics.service;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.target.sm.domain.SMSalesTarget;
import com.alioth.statistics.domain.target.sm.repository.SMSalesTargetRepository;
import com.alioth.statistics.domain.target.team.domain.TeamTarget;
import com.alioth.statistics.domain.target.team.repository.TeamTargetRepository;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
class AchievementRateTest {

    @Autowired private ContractRepository contractRepository;

    @Autowired private SalesMemberRepository salesMemberRepository;
    @Autowired private SMSalesTargetRepository smSalesTargetRepository;

    @Autowired private TeamTargetRepository teamTargetRepository;
    @Autowired private TeamRepository teamRepository;


    @Test
    @DisplayName("개인 달성율")
    public void 개인달성율() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        Map<SalesMembers, String> result = new LinkedHashMap<>();

        for (var member : memberList) {
            BigDecimal contractSum = contractRepository.findBySalesMembers(member).stream()
                    .map(x -> new BigDecimal(x.getContractTotalPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal targetSum = smSalesTargetRepository.findBySalesMembers(member).stream()
                    .map(m -> BigDecimal.valueOf(m.getTargetPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal mulPercent = new BigDecimal("100");
            BigDecimal temp = contractSum.divide(targetSum, 3, RoundingMode.HALF_EVEN);
            temp = temp.multiply(mulPercent);
            result.put(member, temp.toString());
        }

        System.out.println("개인 목표 퍼센트" + result);
    }

    @Test
    @DisplayName("개인 달성건수")
    public void 개인달성건() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        List<Contract> memberByContractList = contractRepository.findBySalesMembers(memberList.get(2));
        List<SMSalesTarget> memberByTargetList = smSalesTargetRepository.findBySalesMembers(memberList.get(2));
        Map<SalesMembers, String> result = new LinkedHashMap<>();

        for (var member : memberList) {
            int size = contractRepository.findBySalesMembers(member).size();
            Long memberTargetCount = smSalesTargetRepository.findBySalesMembers(member).stream()
                    .map(SMSalesTarget::getTargetCount)
                    .reduce(0L, Long::sum);

            double temp = ((double)memberTargetCount / (double)size) * 100;
            String res = String.format("%.3f", temp);
            result.put(member, res + "%");
        }

        System.out.println("result = " + result);
    }


    @Test
    public void memberCreate() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        List<Contract> bySalesMembers = contractRepository.findBySalesMembers(memberList.get(0));



    }






}
