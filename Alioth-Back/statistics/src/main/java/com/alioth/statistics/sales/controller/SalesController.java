package com.alioth.statistics.sales.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.sales.dto.res.*;
import com.alioth.statistics.sales.service.SalesHQService;
import com.alioth.statistics.sales.service.SalesMemberService;
import com.alioth.statistics.sales.service.SalesTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class SalesController {

    private final SalesMemberService salesMemberService;
    private final SalesTeamService salesTeamService;
    private final SalesHQService salesHQService;

    @GetMapping("/statistics/api/stat/sales/{memberCode}/{date}")
    public ResponseEntity<CommonResponse> getPriceTargetMonth(@PathVariable Long memberCode,
                                                              @PathVariable String date) {
        List<SalesMemberMonthResDto> dto = salesMemberService.memberSalesMonth(memberCode, date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 매출",
                dto);
    }


    @GetMapping("/statistics/api/stat/sales/{memberCode}/{date}/price")
    public ResponseEntity<CommonResponse> getMemberPrice(@PathVariable Long memberCode,
                                                              @PathVariable String date) {
        SalesMemberTotalPriceRedDto dto = salesMemberService.memberSalesPrice(memberCode, date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 매출 실적",
                dto);
    }


    @GetMapping("/statistics/api/stat/sales/{memberCode}/{date}/target")
    public ResponseEntity<CommonResponse> getMemberTarget(@PathVariable Long memberCode,
                                                         @PathVariable String date) {
        //Long target = salesMemberService.salesMemberTarget(memberCode, date);
        Long target = salesMemberService.salesMemberTarget(memberCode);
        Long price = salesMemberService.memberSalesTargetResPrice(memberCode, date);

        SalesMemberTargetResDto dto = SalesMemberTargetResDto.builder()
                .target(target)
                .price(price)
                .build();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "사원 매출 목표",
                dto);
    }


    @GetMapping("/statistics/api/sales/{memberTeamCode}/{date}/target")
    public ResponseEntity<CommonResponse> getTeamTarget(@PathVariable String memberTeamCode, @PathVariable String date) {
        SalesTeamTargetResDto dto = salesTeamService.teamTarget(memberTeamCode, date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 매출 목표",
                dto);
    }


    @GetMapping("/statistics/api/sales/{memberTeamCode}/{date}/price")
    public ResponseEntity<CommonResponse> getTeamPrice(@PathVariable String memberTeamCode, @PathVariable String date) {
        SalesTeamTotalPriceResDto dto = salesTeamService.teamTotalPrice(memberTeamCode, date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 매출 가격",
                dto);
    }


    @GetMapping("/statistics/api/sales/hq/{date}/price")
    public ResponseEntity<CommonResponse> getSalesHQTotalPrice(@PathVariable String date) {
        SalesHQTotalPriceResDto dto = salesHQService.getSalesHQTotalPrice(date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전사 매출 전체 현황",
                dto);
    }

    @GetMapping("/statistics/api/sales/hq/{date}/team-price")
    public ResponseEntity<CommonResponse> getTeamSalesTotal(@PathVariable String date) {
        List<SalesHQTotalTeamPriceResDto> dto = salesHQService.getTeamSalesTotal(date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전사가 보는 팀 리스트 매출",
                dto);
    }

}
