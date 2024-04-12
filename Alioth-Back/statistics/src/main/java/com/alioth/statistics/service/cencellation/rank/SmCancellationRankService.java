package com.alioth.statistics.service.cencellation.rank;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.service.cencellation.rank.CancellationRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SmCancellationRankService implements CancellationRankService {

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;

    @Override
    public Map<SalesMembers, String> cancelMoney() {
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

        return result;
    }

    @Override
    public Map<SalesMembers, String> cancelCount() {
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

        return result;
    }
}
