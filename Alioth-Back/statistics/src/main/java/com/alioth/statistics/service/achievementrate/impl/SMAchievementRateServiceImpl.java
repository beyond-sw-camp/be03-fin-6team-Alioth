package com.alioth.statistics.service.achievementrate.impl;

import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.service.achievementrate.SMAchievementRateService;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.target.sm.domain.SMSalesTarget;
import com.alioth.statistics.domain.target.sm.repository.SMSalesTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SMAchievementRateServiceImpl implements SMAchievementRateService {

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;
    private final SMSalesTargetRepository smSalesTargetRepository;

    /* 팀 달성률 */
    @Override
    public Map<SalesMembers, String> achievementRatePercent() {
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

        return result;
    }



    /* 팀 달성 건수 */
    @Override
    public Map<SalesMembers, String> achievementRateCount() {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        //List<Contract> memberByContractList = contractRepository.findBySalesMembers(memberList.get(2));
        //List<SMSalesTarget> memberByTargetList = smSalesTargetRepository.findBySalesMembers(memberList.get(2));
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

        return result;
    }
}
