package com.alioth.statistics.service.cencellation.impl;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SmCancellationRankServiceTest {

    @Autowired private ContractRepository contractRepository;
    @Autowired private SalesMemberRepository salesMemberRepository;

    @Test
    @DisplayName("사원 해약 금액 순위")
    @Transactional
    public void 사원해약금액순위() {

        Map<SalesMembers, String> temp = new HashMap<>();
        Map<SalesMembers, String> result = new LinkedHashMap<>();
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        for (var member : memberList) {

            List<Contract> contractList = contractRepository.findBySalesMembers(member);

            BigDecimal cancelPrice = contractList.stream()
                    .filter(x -> x.getContractStatus() == ContractStatus.Cancellation)
                    .map(x -> new BigDecimal(x.getContractTotalPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            temp.put(member, cancelPrice.toString());
        }

        List<SalesMembers> keys = new ArrayList<>(temp.keySet());
        Collections.sort(keys, (v1, v2) -> (temp.get(v2).compareTo(temp.get(v1))));
        for (var key : keys) {
            System.out.println(key.getName() + " : " + temp.get(key));
            result.put(key, temp.get(key));
        }

    }


    @Test
    @DisplayName("사원 해약 건 순위")
    @Transactional
    public void 사원해약건순위() {
        Map<SalesMembers, String> temp = new HashMap<>();
        Map<SalesMembers, String> result = new LinkedHashMap<>();
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        for (var member : memberList) {

            List<Contract> contractList = contractRepository.findBySalesMembers(member);

            long count = contractList.stream()
                    .filter(x -> x.getContractStatus() == ContractStatus.Cancellation)
                    .count();

            temp.put(member, String.valueOf(count));
        }

        List<SalesMembers> keys = new ArrayList<>(temp.keySet());
        Collections.sort(keys, (v1, v2) -> (temp.get(v2).compareTo(temp.get(v1))));
        for (var key : keys) {
            System.out.println(key.getName() + " : " + temp.get(key));
            result.put(key, temp.get(key));
        }
    }

}