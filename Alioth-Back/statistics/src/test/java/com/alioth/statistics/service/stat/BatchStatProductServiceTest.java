package com.alioth.statistics.service.stat;

import com.alioth.statistics.domain.batch.BatchRankProduct;
import com.alioth.statistics.domain.batch.dto.res.BatchProductCountResDto;
import com.alioth.statistics.domain.batch.dto.res.BatchProductPriceResDto;
import com.alioth.statistics.domain.batch.repository.BatchRankProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootTest
class BatchStatProductServiceTest {

    @Autowired private BatchRankProductRepository productRepository;

    @Test
    @DisplayName("일별 보험 상품 가격")
    public void 일별보험상품가격() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear(); int month = now.getMonthValue(); int day = now.getDayOfMonth(); int hour = 0; int minute = 0;
        LocalDateTime endTime = LocalDateTime.of(year, month, 10, hour, minute);
        LocalDateTime startTime = endTime.minusDays(1L);

        Map<String, List<BatchRankProduct>> collect = productRepository.findByCreatedDateBetween(startTime, endTime)
                .stream()
                .collect(Collectors.groupingBy(BatchRankProduct::getProductCategory));

        List<BatchProductPriceResDto> dto = new LinkedList<>();
        for(var key : collect.keySet()) {
            BigInteger sum = collect.get(key).stream()
                    .map(product -> new BigInteger(product.getContractPrice()))
                    .reduce(BigInteger.ZERO, BigInteger::add);

            BatchProductPriceResDto temp = BatchProductPriceResDto.builder()
                    .category(key)
                    .price(sum.toString())
                    .build();

            dto.add(temp);
        }

        System.out.println("dto = " + dto);
    }


    @Test
    public void testMondayCheck() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear(); int month = now.getMonthValue(); int day = now.getDayOfMonth(); int hour = 0; int minute = 0;
        LocalDateTime endTime = LocalDateTime.of(year, month, 10, hour, minute);
        LocalDateTime startTime = endTime.minusDays(1L);

        Map<String, List<BatchRankProduct>> collect = productRepository.findByCreatedDateBetween(startTime, endTime)
                .stream()
                .collect(Collectors.groupingBy(BatchRankProduct::getProductCategory));

        List<BatchProductCountResDto> dto = new LinkedList<>();
        for(var key : collect.keySet()) {
            Long contractCount = collect.get(key).stream()
                    .map(product -> Long.valueOf(product.getContractCount()))
                    .reduce(0L, Long::sum);

            BatchProductCountResDto temp = BatchProductCountResDto.builder()
                    .category(key)
                    .count(contractCount)
                    .build();

            dto.add(temp);
        }

        System.out.println("dto = " + dto);
    }


    @Test
    public void testWeekendCheck() {
        LocalDateTime now = LocalDateTime.now();
        int value = now.getMonth().getValue();





    }








}