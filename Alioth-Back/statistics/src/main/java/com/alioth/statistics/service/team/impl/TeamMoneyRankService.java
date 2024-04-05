package com.alioth.statistics.service.team.impl;


import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import com.alioth.statistics.service.team.TeamRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamMoneyRankService implements TeamRankService {

    private final ContractRepository contractRepository;
    private final TeamRepository teamRepository;


    /* 팀 달성 금액 순위 */
    @Override
    public Map<Team, BigInteger> teamMoneyRank() {

        Map<Team, BigInteger> temp = new HashMap<>();
        Map<Team, BigInteger> result = new LinkedHashMap<>();
        List<Team> teamList = teamRepository.findAll();

        for (var team : teamList) {
            List<SalesMembers> memberList = team.getTeamMembers();
            BigInteger priceSum = BigInteger.ZERO;
            for (var member : memberList) {
                priceSum = contractRepository.findBySalesMembers(member).stream()
                        .map(x -> new BigInteger(x.getContractTotalPrice()))
                        .reduce(BigInteger::add)
                        .orElse(BigInteger.ZERO);
            }
            temp.put(team, priceSum);
        }

        List<Team> keys = new ArrayList<>(temp.keySet());
        Collections.sort(keys, (v1, v2) -> (temp.get(v2).compareTo(temp.get(v1))));

        for (var key : keys) {
            System.out.println(key.getTeamCode() + " : " + temp.get(key));
            result.put(key, temp.get(key));
        }

        return result;
    }
}
