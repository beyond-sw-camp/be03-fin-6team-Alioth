package com.alioth.statistics.service.cencellation.impl;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import com.alioth.statistics.service.cencellation.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HqCancellationService implements CancellationService {

    private final ContractRepository contractRepository;
    private final TeamRepository teamRepository;

    @Override
    public Map<?, String> cancelMoneyPercent() {
        Map<String, String> result = new LinkedHashMap<>();
        List<Team> teamList = teamRepository.findAll();

        BigDecimal teamTotalPrice = BigDecimal.ZERO;
        BigDecimal teamCancelPrice = BigDecimal.ZERO;

        for (var team : teamList) {

            List<SalesMembers> teamMembers = team.getTeamMembers();
            for (var member : teamMembers) {

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

                teamTotalPrice = teamTotalPrice.add(totalPrice);
                teamCancelPrice = teamCancelPrice.add(cancelPrice);
            }
        }

        BigDecimal percent = new BigDecimal("100");
        BigDecimal divide = teamCancelPrice.divide(teamTotalPrice, 3, RoundingMode.HALF_EVEN);
        BigDecimal multiply = divide.multiply(percent);
        result.put("전사", multiply + "%");

        return result;
    }

    @Override
    public Map<?, String> cancelCountPercent() {
        Map<String, String> result = new LinkedHashMap<>();
        List<Team> teamList = teamRepository.findAll();

        Long teamTotalCount = 0L;
        Long teamCancelCount = 0L;

        for (var team : teamList) {

            List<SalesMembers> teamMembers = team.getTeamMembers();
            for (var member : teamMembers) {

                List<Contract> contractList = contractRepository.findBySalesMembers(member);
                long totalPrice = contractList.stream().count();

                long cancelPrice = contractList.stream()
                        .filter(x -> x.getContractStatus() == ContractStatus.Cancellation)
                        .count();

                teamTotalCount += totalPrice;
                teamCancelCount += cancelPrice;
            }
        }

        double v = ((double) teamCancelCount / (double) teamTotalCount) * 100;
        String strResult = String.format("%.3f", v);
        result.put("전사", strResult + "%");

        return result;
    }
}
