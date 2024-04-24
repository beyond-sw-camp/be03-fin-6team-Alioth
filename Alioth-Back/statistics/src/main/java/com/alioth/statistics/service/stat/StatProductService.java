package com.alioth.statistics.service.stat;


import com.alioth.statistics.domain.batch.BatchRankProduct;
import com.alioth.statistics.domain.batch.dto.res.BatchProductCountResDto;
import com.alioth.statistics.domain.batch.dto.res.BatchProductPriceResDto;
import com.alioth.statistics.domain.batch.repository.BatchRankProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatProductService {

    private final BatchRankProductRepository productRepository;


    public List<BatchProductPriceResDto> productDayPrice() {
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
        
        return dto;
    }







    public List<BatchProductCountResDto> productDayCount() {
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

        return dto;
    }






}
