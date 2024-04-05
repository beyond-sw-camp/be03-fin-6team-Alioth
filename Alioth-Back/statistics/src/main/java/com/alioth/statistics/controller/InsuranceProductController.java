package com.alioth.statistics.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.service.insuranceproduct.InsuranceProductService;
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
public class InsuranceProductController {

    private final InsuranceProductService insuranceProductService;


    @GetMapping("/insurance/contract")
    public ResponseEntity<CommonResponse> insuranceContractRank() {
        Map<String, String> result = insuranceProductService.insuranceContractRankService();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "보험 계약 순위 입니다.",
                result
        );
    }


    @GetMapping("/insurance/product")
    public ResponseEntity<CommonResponse> insuranceProductRank() {
        Map<String, Integer> result = insuranceProductService.insuranceProductRankService();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "보험 계약 순위 입니다.",
                result
        );
    }


}