package com.alioth.statistics.sales.controller;

import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.batch.dto.res.BatchMemberSalesResDto;
import com.alioth.statistics.domain.batch.dto.res.BatchProductCountResDto;
import com.alioth.statistics.domain.batch.dto.res.BatchProductPriceResDto;
import com.alioth.statistics.sales.dto.res.SalesMemberRankResDto;
import com.alioth.statistics.sales.service.SalesRankingService;
import com.alioth.statistics.service.stat.StatProductService;
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
public class SalesRankingController {

    private final SalesRankingService salesRankingService;
    private final StatProductService productService;

    /* 기본 월 표시 */
    @GetMapping("/statistics/api/stat/sales-ranking/member")
    public ResponseEntity<CommonResponse> memberSalesMonth() {
        List<SalesMemberRankResDto> dto = salesRankingService.memberSalesMonth();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "개인 월 매출입니다.",
                dto
        );
    }

    /* 기본 월 표시 */

    @GetMapping("/statistics/api/stat/sales-ranking/member/{date}")
    public ResponseEntity<CommonResponse> memberSalesMonth(@PathVariable("date") String date) {
        List<SalesMemberRankResDto> dto = salesRankingService.memberSalesMonth(date);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "개인 " + date + "일 매출입니다.",
                dto
        );
    }



    @GetMapping("/statistics/api/batch/contract-rank/price")
    public ResponseEntity<CommonResponse> getRankProductsPrice() {
        List<BatchProductPriceResDto> dto = productService.productDayPrice();

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 금액별 결과입니다", dto);
    }

    @GetMapping("/statistics/api/batch/contract-rank/price/{date}")
    public ResponseEntity<CommonResponse> getRankProductsPrice(@PathVariable("date") String date) {
        List<BatchProductPriceResDto> dto = productService.productDayPrice(date);

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 금액별 결과입니다", dto);
    }


    @GetMapping("/api/batch/contract-rank/count")
    public ResponseEntity<CommonResponse> getRankProductsCount() {
        List<BatchProductCountResDto> dto = productService.productDayCount();

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 금액건 결과입니다", dto);
    }


    @GetMapping("/statistics/api/batch/contract-rank/count/{date}")
    public ResponseEntity<CommonResponse> getRankProductsCount(@PathVariable("date") String date) {
        List<BatchProductCountResDto> dto = productService.productDayCount(date);

        return CommonResponse.responseMessage(HttpStatus.OK, "보험별 금액건 결과입니다", dto);
    }





}
