package com.alioth.statistics.service.salesmember.impl;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberAchieveRankServiceTest {

    @Autowired private ContractRepository contractRepository;
    @Autowired private SalesMemberRepository salesMemberRepository;

    @Test
    @DisplayName("사원 달성 금액 순위")
    public void 사원달성금액순위() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        Map<SalesMembers, BigInteger> temp = new HashMap<>();
        Map<SalesMembers, BigInteger> result = new LinkedHashMap<>();

        for (var member : memberList) {
            BigInteger memberTotalPrice = contractRepository.findBySalesMembers(member).stream()
                    .map(x -> new BigInteger(x.getContractTotalPrice()))
                    .reduce(BigInteger::add)
                    .orElse(BigInteger.ZERO);

            temp.put(member, memberTotalPrice);
        }

        List<SalesMembers> keys = new ArrayList<>(temp.keySet());
        Collections.sort(keys, (v1, v2) -> (temp.get(v2).compareTo(temp.get(v1))));

        for (var key : keys) {
            System.out.println(key.getName() + " : " + temp.get(key));
            result.put(key, temp.get(key));
        }
    }


    @Test
    @DisplayName("사원 달성 계약 건 순위")
    public void 사원달성계약건순위() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        Map<SalesMembers, Long> temp = new HashMap<>();
        Map<SalesMembers, Long> result = new LinkedHashMap<>();

        for (var member : memberList) {
            long size = contractRepository.findBySalesMembers(member).size();
            temp.put(member, size);
        }

        List<SalesMembers> keys = new ArrayList<>(temp.keySet());
        Collections.sort(keys, (v1, v2) -> (temp.get(v2).compareTo(temp.get(v1))));

        for (var key : keys) {
            System.out.println(key.getName() + " : " + temp.get(key));
            result.put(key, temp.get(key));
        }
    }

}