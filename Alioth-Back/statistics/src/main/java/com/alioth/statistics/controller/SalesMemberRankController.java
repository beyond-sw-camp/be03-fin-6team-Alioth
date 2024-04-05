package com.alioth.statistics.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.service.salesmember.AchieveRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Map;

@RequestMapping("/stat")
@RestController
@RequiredArgsConstructor
public class SalesMemberRankController {

    private final AchieveRankService achieveRankService;


    @GetMapping("/sm/rank/money")
    public ResponseEntity<CommonResponse> achieveMoneyRank() {
        Map<SalesMembers, BigInteger> result = achieveRankService.memberAchieveMoneyRank();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 달성 금액 순위입니다.",
                result
        );
    }


    @GetMapping("/sm/rank/count")
    public ResponseEntity<CommonResponse> achieveCountRank() {
        Map<SalesMembers, Long> result = achieveRankService.memberAchieveCountRank();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 달성 계약 건 순위입니다.",
                result
        );
    }



}
