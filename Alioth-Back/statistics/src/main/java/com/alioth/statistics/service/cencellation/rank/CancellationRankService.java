package com.alioth.statistics.service.cencellation.rank;

import com.alioth.statistics.domain.member.domain.SalesMembers;

import java.util.Map;

public interface CancellationRankService {

    Map<SalesMembers, String> cancelMoney();
    Map<SalesMembers, String> cancelCount();

}
