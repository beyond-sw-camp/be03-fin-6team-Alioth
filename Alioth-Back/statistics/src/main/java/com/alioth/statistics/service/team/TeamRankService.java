package com.alioth.statistics.service.team;

import com.alioth.statistics.domain.team.domain.Team;

import java.math.BigInteger;
import java.util.Map;

public interface TeamRankService {

    Map<Team, BigInteger> teamMoneyRank();

}
