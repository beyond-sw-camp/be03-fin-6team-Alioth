package com.alioth.statistics.sales.controller;

import com.alioth.statistics.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SalesTeamController {

    @GetMapping("/api/stat/sales/team/day")
    public ResponseEntity<CommonResponse> getPriceTargetDay() {


        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전사 월 매출",
                null);
    }

    @GetMapping("/api/stat/sales/team/month")
    public ResponseEntity<CommonResponse> getPriceTargetMonth() {


        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전사 반기 매출",
                null);
    }

    @GetMapping("/api/stat/sales/team/year")
    public ResponseEntity<CommonResponse> getPriceTargetYear() {


        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전사 년 매출",
                null);
    }

}
