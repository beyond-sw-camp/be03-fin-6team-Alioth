package com.alioth.statistics.service.salesmember;

import com.alioth.statistics.domain.member.domain.SalesMembers;

import java.math.BigInteger;
import java.util.Map;

public interface AchieveRankService {
    Map<SalesMembers, BigInteger> memberAchieveMoneyRank();
    Map<SalesMembers, Long> memberAchieveCountRank();
}
