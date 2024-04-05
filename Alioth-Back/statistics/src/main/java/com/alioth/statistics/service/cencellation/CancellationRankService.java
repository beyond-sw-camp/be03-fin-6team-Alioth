package com.alioth.statistics.service.cencellation;

import com.alioth.statistics.domain.member.domain.SalesMembers;

import java.math.BigInteger;
import java.util.Map;

public interface CancellationRankService {

    Map<SalesMembers, String> cancelMoney();
    Map<SalesMembers, String> cancelCount();

}
