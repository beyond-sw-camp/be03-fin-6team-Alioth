package com.alioth.statistics.service.achievementrate.impl;

import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.service.achievementrate.TeamAchievementRateService;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.target.team.domain.TeamTarget;
import com.alioth.statistics.domain.target.team.repository.TeamTargetRepository;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeamAchievementRateServiceImpl implements TeamAchievementRateService {

    private final ContractRepository contractRepository;
    private final TeamRepository teamRepository;
    private final TeamTargetRepository teamTargetRepository;



    /* 개인별 달성률 */
    @Override
    public Map<Team, String> achievementRatePercent() {
        List<Team> teamList = teamRepository.findAll();

        Map<Team, String> result = new HashMap<>();

        for (var team : teamList) {
            /* ----팀 목표 금액 계산--- */
            Team getTeam = teamRepository.findByTeamCode(team.getTeamCode());
            List<TeamTarget> teamTargetList = teamTargetRepository.findByTeam(getTeam);
            BigInteger teamTargetSum = BigInteger.ZERO;

            for (var teamTarget : teamTargetList) {
                teamTargetSum = teamTargetSum.add(teamTarget.getTargetPrice());
            }
            /* ----팀 목표 금액 계산--- */


            /* ----개인 전체 금액 계산--- */
            List<SalesMembers> teamMembers = getTeam.getTeamMembers();
            BigInteger smContractSum = BigInteger.ZERO;

            for (var member : teamMembers) {
                BigInteger contractSum = contractRepository.findBySalesMembers(member).stream()
                        .map(x -> new BigInteger(x.getContractTotalPrice()))
                        .reduce(BigInteger.ZERO, BigInteger::add);

                smContractSum = smContractSum.add(contractSum);
            }
            /* ----개인 전체 금액 계산--- */

            BigDecimal decimalTeamTargetSum = new BigDecimal(teamTargetSum);
            BigDecimal decimalSMContractSum = new BigDecimal(smContractSum);

            BigDecimal mulPercent = new BigDecimal("100");
            BigDecimal divide = decimalSMContractSum.divide(decimalTeamTargetSum, 3, RoundingMode.HALF_EVEN);

            BigDecimal temp = divide.multiply(mulPercent);
            result.put(team, temp.toString() + "%");
        }

        return result;
    }


    /* 개인별 달성 건수 */
    @Override
    public Map<Team, String> achievementRateCount() {

        List<Team> teamList = teamRepository.findAll();
        Map<Team, String> result = new HashMap<>();

        for (var team : teamList) {

            /* ----팀 목표 건 계산--- */
            Team getTeam = teamRepository.findByTeamCode(team.getTeamCode());
            List<TeamTarget> teamTargetList = teamTargetRepository.findByTeam(getTeam);
            Long teamTargetCount = 0L;

            for (var teamTarget : teamTargetList) {
                teamTargetCount += teamTarget.getTargetCount();
            }
            /* ----팀 목표 건 계산--- */


            /* ----개인 전체 건 계산--- */
            int allContractSize = 0;
            for (var member : getTeam.getTeamMembers()) {
                int size = contractRepository.findBySalesMembers(member).size();
                allContractSize += size;
            }
            /* ----개인 전체 건 계산--- */

            double temp = ((double)allContractSize / (double)teamTargetCount) * 100;
            String strResult = String.format("%.3f", temp);
            result.put(team, strResult);
        }

        System.out.println("result = " + result);


        return null;
    }
}
