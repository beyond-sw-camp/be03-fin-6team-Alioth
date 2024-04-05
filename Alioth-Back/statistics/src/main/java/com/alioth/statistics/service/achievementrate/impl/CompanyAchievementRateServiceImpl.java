package com.alioth.statistics.service.achievementrate.impl;

import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.service.achievementrate.CompanyAchievementRateService;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.target.team.domain.TeamTarget;
import com.alioth.statistics.domain.target.team.repository.TeamTargetRepository;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CompanyAchievementRateServiceImpl implements CompanyAchievementRateService {

    private final ContractRepository contractRepository;
    private final TeamRepository teamRepository;
    private final TeamTargetRepository teamTargetRepository;

    @Override
    public String achievementRatePercent() {
        List<Team> teamList = teamRepository.findAll();
        String result = "";
        BigInteger teamTargetSum = BigInteger.ZERO;
        BigInteger smContractSum = BigInteger.ZERO;

        for (var team : teamList) {
            /* ----팀 목표 금액 계산--- */
            Team getTeam = teamRepository.findByTeamCode(team.getTeamCode());
            List<TeamTarget> teamTargetList = teamTargetRepository.findByTeam(getTeam);

            for (var teamTarget : teamTargetList) {
                teamTargetSum = teamTargetSum.add(teamTarget.getTargetPrice());
            }
            /* ----팀 목표 금액 계산--- */

            /* ----개인 전체 금액 계산--- */
            List<SalesMembers> teamMembers = getTeam.getTeamMembers();

            for (var member : teamMembers) {
                BigInteger contractSum = contractRepository.findBySalesMembers(member).stream()
                        .map(x -> new BigInteger(x.getContractTotalPrice()))
                        .reduce(BigInteger.ZERO, BigInteger::add);

                smContractSum = smContractSum.add(contractSum);
            }
            /* ----개인 전체 금액 계산--- */
        }

        BigDecimal temp1 = new BigDecimal(teamTargetSum);
        BigDecimal temp2 = new BigDecimal(smContractSum);

        BigDecimal divide = temp1.divide(temp2, 5, RoundingMode.HALF_EVEN);
        result = divide.toString();
        Double v = Double.valueOf(result);
        System.out.println("result = " + v * 100 + "%");

        return v * 100 + "%";
    }

    @Override
    public String achievementRateCount() {
        List<Team> teamList = teamRepository.findAll();
        Long teamTargetCountSum = 0L;
        Long smContractCount = 0L;

        for (var team : teamList) {
            /* ----팀 목표 금액 계산--- */
            Team getTeam = teamRepository.findByTeamCode(team.getTeamCode());
            List<TeamTarget> teamTargetList = teamTargetRepository.findByTeam(getTeam);

            for (var teamTarget : teamTargetList) {
                teamTargetCountSum += teamTarget.getTargetCount();
            }
            /* ----팀 목표 금액 계산--- */

            /* ----개인 전체 금액 계산--- */
            List<SalesMembers> teamMembers = getTeam.getTeamMembers();

            for (var member : teamMembers) {
                long sum = contractRepository.findBySalesMembers(member).size();

                smContractCount += sum;
            }
            /* ----개인 전체 금액 계산--- */
        }

        System.out.println("teamTargetCountSum = " + teamTargetCountSum);
        System.out.println("smContractCount = " + smContractCount);
        double v = ((double) smContractCount / (double) teamTargetCountSum) * 100;
        String result = String.format("%.3f", v);
        System.out.println("result = " + result);

        return result;
    }
}
