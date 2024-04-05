package com.alioth.statistics.service;

import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.target.sm.repository.SMSalesTargetRepository;
import com.alioth.statistics.domain.target.team.domain.TeamTarget;
import com.alioth.statistics.domain.target.team.repository.TeamTargetRepository;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class AchievementRateTeamTest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired private TeamRepository teamRepository;
    @Autowired private TeamTargetRepository teamTargetRepository;

    @Test
    @DisplayName("팀 달성율")
    @Transactional
    public void 팀달성율() {
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

        System.out.println("result = " + result);
    }


    @Test
    @DisplayName("팀 달성건")
    @Transactional
    public void 팀달성건() {

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
            result.put(team, strResult + "%");
        }

        System.out.println("result = " + result);
    }

}
