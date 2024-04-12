package com.alioth.statistics.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.insurance.dto.res.InsuranceGodResDto;
import com.alioth.statistics.service.insurance.InsuranceGodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/stat")
@RestController
@RequiredArgsConstructor
public class InsuranceGodController {

    private final InsuranceGodService insuranceGodServiceImpl;


    @GetMapping("/insurance/god/month")
    public ResponseEntity<CommonResponse> insuranceOfGodMonth() {
        InsuranceGodResDto result = insuranceGodServiceImpl.insuranceOfGodMonth();
        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonth().getValue();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                year + "년 " + month + " 월" + " 보험의 신입니다.",
                result
        );
    }

    @GetMapping("/insurance/god/quarter")
    public ResponseEntity<CommonResponse> insuranceOfGodQuarter() {
        InsuranceGodResDto result = insuranceGodServiceImpl.insuranceOfGodQuarter();
        int month = LocalDateTime.now().getMonth().firstMonthOfQuarter().getValue();
        String quarter = month - 6 > 0 ? "하" : "상";

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "이번 " + quarter + "반기 보험의 신입니다.",
                result
        );
    }


    @GetMapping("/insurance/god/year")
    public ResponseEntity<CommonResponse> insuranceOfGodYear() {
        InsuranceGodResDto result = insuranceGodServiceImpl.insuranceOfGodYear();
        int year = LocalDateTime.now().getYear();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                year + "년 " + "올해의 보험의 신입니다.",
                result
        );
    }

}