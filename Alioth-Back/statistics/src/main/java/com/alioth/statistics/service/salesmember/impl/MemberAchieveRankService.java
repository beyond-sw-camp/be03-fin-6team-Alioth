package com.alioth.statistics.service.salesmember.impl;

import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.service.salesmember.AchieveRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberAchieveRankService implements AchieveRankService {

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;

    @Override
    public Map<SalesMembers, BigInteger> memberAchieveMoneyRank() {
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

        return result;
    }

    @Override
    public Map<SalesMembers, Long> memberAchieveCountRank() {
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

        return result;
    }
}
