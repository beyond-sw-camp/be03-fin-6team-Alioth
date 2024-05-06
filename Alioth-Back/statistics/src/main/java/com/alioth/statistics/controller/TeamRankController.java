package com.alioth.statistics.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.service.team.TeamRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Map;

@RequestMapping("/statistics")
@RestController
@RequiredArgsConstructor
public class TeamRankController {

    private final TeamRankService teamRankService;

    @GetMapping("/team/rank/money")
    public ResponseEntity<CommonResponse> teamMoneyRank() {
        Map<Team, BigInteger> result = teamRankService.teamMoneyRank();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 달성 금액 순위입니다.",
                result
        );
    }


}
