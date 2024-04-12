package com.alioth.statistics.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.service.cencellation.rank.CancellationRankService;
import com.alioth.statistics.service.cencellation.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/stat")
@RestController
@RequiredArgsConstructor
public class CancellationController {

    private final CancellationService smCancellationService;
    private final CancellationService teamCancellationService;
    private final CancellationService hqCancellationService;
    private final CancellationRankService smCancellationRankService;

    @GetMapping("/cancel/sm/money")
    public ResponseEntity<CommonResponse> smCancelMoneyPercent() {
        Map<?, String> result = smCancellationService.cancelMoneyPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원별 해약금액 % 입니다.",
                result
        );
    }

    @GetMapping("/cancel/sm/count")
    public ResponseEntity<CommonResponse> smCancelCountPercent() {
        Map<?, String> result = smCancellationService.cancelCountPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원별 해약건 % 입니다.",
                result
        );
    }



    @GetMapping("/cancel/sm/rank/money")
    public ResponseEntity<CommonResponse> smCancelMoney() {
        Map<SalesMembers, String> result = smCancellationRankService.cancelMoney();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원별 해약금액 순위 입니다.",
                result
        );
    }

    @GetMapping("/cancel/sm/rank/count")
    public ResponseEntity<CommonResponse> smCancelCount() {
        Map<SalesMembers, String> result = smCancellationRankService.cancelCount();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원별 해약건 순위 입니다.",
                result
        );
    }




    @GetMapping("/cancel/team/money")
    public ResponseEntity<CommonResponse> teamCancelMoneyPercent() {
        Map<?, String> result = teamCancellationService.cancelMoneyPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 해약금액 % 입니다.",
                result
        );
    }

    @GetMapping("/cancel/team/count")
    public ResponseEntity<CommonResponse> teamCancelCountPercent() {
        Map<?, String> result = teamCancellationService.cancelCountPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 해약건 % 입니다.",
                result
        );
    }


    @GetMapping("/cancel/hq/money")
    public ResponseEntity<CommonResponse> hqCancelMoneyPercent() {
        Map<?, String> result = hqCancellationService.cancelMoneyPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전사 해약금액 % 입니다.",
                result
        );
    }

    @GetMapping("/cancel/hq/count")
    public ResponseEntity<CommonResponse> hqCancelCountPercent() {
        Map<?, String> result = hqCancellationService.cancelCountPercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전사 해약건 % 입니다.",
                result
        );
    }












}
