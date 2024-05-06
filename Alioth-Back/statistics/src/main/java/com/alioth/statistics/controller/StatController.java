package com.alioth.statistics.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.batch.dto.res.*;
import com.alioth.statistics.service.stat.StatHQSalesService;
import com.alioth.statistics.service.stat.StatMemberSalesService;
import com.alioth.statistics.service.stat.StatProductService;
import com.alioth.statistics.service.stat.StatTeamSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatMemberSalesService memberSalesService;
    private final StatTeamSalesService teamSalesService;
    private final StatHQSalesService hqSalesService;
    private final StatProductService productService;


    @GetMapping("/statistics/api/batch/sales-member/day")
    public ResponseEntity<CommonResponse> memberSalesDay() {
        List<BatchMemberSalesResDto> dto = memberSalesService.memberSalesMonth();

        return CommonResponse.responseMessage(HttpStatus.OK, "사원 일별 결과입니다", dto);
    }


    @GetMapping("/statistics/api/batch/sales-member/month")
    public ResponseEntity<CommonResponse> memberSalesMonth() {
        List<BatchMemberSalesResDto> dto = memberSalesService.memberSalesMonth();

        List<BatchMemberSalesResDto> tempList = new LinkedList<>();
        BatchMemberSalesResDto temp = BatchMemberSalesResDto.builder()
                .salesMemberName("손흥민")
                .salesMemberCode("7")
                .contractPrice("11111")
                .contractCount("22222")
                .cancelPrice("33333")
                .cancelCount("44444")
                .build();
        tempList.add(temp);

        return CommonResponse.responseMessage(HttpStatus.OK, "사원 월별 결과입니다", tempList);
    }

    @GetMapping("/statistics/api/batch/sales-member/quarter")
    public ResponseEntity<CommonResponse> memberSalesQuarter() {
        List<BatchMemberSalesResDto> dto = memberSalesService.memberSalesQuarter();

        return CommonResponse.responseMessage(HttpStatus.OK, "사원 반기별 결과입니다", dto);
    }

    @GetMapping("/statistics/api/batch/sales-member/year")
    public ResponseEntity<CommonResponse> memberSalesYear() {
        List<BatchMemberSalesResDto> dto = memberSalesService.memberSalesYear();

        return CommonResponse.responseMessage(HttpStatus.OK, "사원 년별 결과입니다", dto);
    }




    @GetMapping("/statistics/api/batch/sales-team/day")
    public ResponseEntity<CommonResponse> getTeamSalesDay() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesDay();

        List<BatchTeamSalesResDto> tempList = new LinkedList<>();
        BatchTeamSalesResDto temp1 = BatchTeamSalesResDto.builder()
                .teamName("보험 1팀")
                .teamCode("11122")
                .contractPrice("11001000")
                .contractCount("1134")
                .cancelPrice("11444")
                .cancelCount("11544")
                .build();

        BatchTeamSalesResDto temp2 = BatchTeamSalesResDto.builder()
                .teamName("보험 2팀")
                .teamCode("2222")
                .contractPrice("32223")
                .contractCount("221")
                .cancelPrice("661123")
                .cancelCount("666")
                .build();

        BatchTeamSalesResDto temp3 = BatchTeamSalesResDto.builder()
                .teamName("보험 3팀")
                .teamCode("5555")
                .contractPrice("123123")
                .contractCount("574")
                .cancelPrice("7777")
                .cancelCount("44")
                .build();

        tempList.add(temp1);
        tempList.add(temp2);
        tempList.add(temp3);

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 일별 결과입니다", tempList);
    }

    @GetMapping("/statistics/api/batch/sales-team/month")
    public ResponseEntity<CommonResponse> getTeamSalesMonth() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesMonth();

        List<BatchTeamSalesResDto> tempList = new LinkedList<>();
        BatchTeamSalesResDto temp = BatchTeamSalesResDto.builder()
                .teamName("임시팀이름")
                .teamName("20202020202")
                .contractPrice("23123123")
                .contractCount("11111")
                .cancelPrice("4444444")
                .cancelCount("3333")
                .build();
        tempList.add(temp);

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 월별 결과입니다", tempList);
    }

    @GetMapping("/statistics/api/batch/sales-team/quarter")
    public ResponseEntity<CommonResponse> getTeamSalesQuarter() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesQuarter();

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 반기별 결과입니다", dto);
    }

    @GetMapping("/statistics/api/batch/sales-team/year")
    public ResponseEntity<CommonResponse> getTeamSalesYear() {
        List<BatchTeamSalesResDto> dto = teamSalesService.teamSalesYear();

        return CommonResponse.responseMessage(HttpStatus.OK, "팀 년별 결과입니다", dto);
    }


    @GetMapping("/statistics/api/batch/sales-hq/day")
    public ResponseEntity<CommonResponse> getHQSalesDay() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesDay();

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 일별 결과입니다", dto);
    }

    @GetMapping("/statistics/api/batch/sales-hq/month")
    public ResponseEntity<CommonResponse> getHQSalesMonth() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesMonth();

        List<BatchHQSalesResDto> tempList = new LinkedList<>();
        BatchHQSalesResDto temp = BatchHQSalesResDto.builder()
                .contractPrice("11111")
                .contractCount("3333")
                .cancelPrice("4444444")
                .cancelCount("3333")
                .build();
        tempList.add(temp);

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 월별 결과입니다", tempList);
    }

    @GetMapping("/statistics/api/batch/sales-hq/quarter")
    public ResponseEntity<CommonResponse> getHQSalesQuarter() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesQuarter();

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 반기 결과입니다", dto);
    }

    @GetMapping("/statistics/api/batch/sales-hq/year")
    public ResponseEntity<CommonResponse> getHQSalesYear() {
        List<BatchHQSalesResDto> dto = hqSalesService.hqSalesYear();

        return CommonResponse.responseMessage(HttpStatus.OK, "전사 연별 결과입니다", dto);
    }




    @GetMapping("/statistics/api/batch/contract-rank/price")
    public ResponseEntity<CommonResponse> getRankProductsPrice() {
        List<BatchProductPriceResDto> dto = productService.productDayPrice();

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 일간 금액별 결과입니다", dto);
    }


    @GetMapping("/statistics/api/batch/contract-rank/count")
    public ResponseEntity<CommonResponse> getRankProductsCount() {
        List<BatchProductCountResDto> dto = productService.productDayCount();

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 일간 금액건 결과입니다", dto);
    }
}
