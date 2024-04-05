package com.alioth.statistics.service.cencellation.impl;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.service.cencellation.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class SmCancellationService implements CancellationService {

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;
    private final ContractStatus contractStatus = ContractStatus.Cancellation;

    @Override
    public Map<SalesMembers, String> cancelMoneyPercent() {

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

            result.put(member, multiply+"%");
        }



        return result;
    }

    @Override
    public Map<SalesMembers, String> cancelCountPercent() {
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

        return result;
    }
}
