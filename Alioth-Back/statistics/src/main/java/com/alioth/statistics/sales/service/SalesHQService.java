package com.alioth.statistics.sales.service;

import com.alioth.statistics.domain.batch.BatchHQSales;
import com.alioth.statistics.domain.batch.BatchTeamSales;
import com.alioth.statistics.domain.batch.repository.BatchHQSalesRepository;
import com.alioth.statistics.domain.batch.repository.BatchTeamSalesRepository;
import com.alioth.statistics.sales.dto.res.SalesHQTotalPriceResDto;
import com.alioth.statistics.sales.dto.res.SalesHQTotalTeamPriceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesHQService {

    private final BatchHQSalesRepository hqSalesRepository;
    private final BatchTeamSalesRepository teamSalesRepository;


    public SalesHQTotalPriceResDto getSalesHQTotalPrice(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);

        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        List<BatchHQSales> hqList = hqSalesRepository.findByCreatedDateBetween(startTime, endTime);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Long totalCount = 0L;
        BigDecimal totalCancelPrice = BigDecimal.ZERO;
        Long cancelCount = 0L;

        for (var hqSales : hqList) {
            totalPrice = totalPrice.add(new BigDecimal(hqSales.getTotalPrice()));
            totalCount += Long.valueOf(hqSales.getTotalCount());
            totalCancelPrice = totalCancelPrice.add(new BigDecimal(hqSales.getCancelPrice()));
            cancelCount += Long.valueOf(hqSales.getCancelCount());
        }

        return SalesHQTotalPriceResDto.builder()
                .contractPrice(totalPrice.toEngineeringString())
                .contractCount(String.valueOf(totalCount))
                .cancelPrice(totalCancelPrice.toEngineeringString())
                .cancelCount(String.valueOf(cancelCount))
                .build();
    }



    public List<SalesHQTotalTeamPriceResDto> getTeamSalesTotal(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);

        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        Map<String, List<BatchTeamSales>> collect = teamSalesRepository.findByCreatedTimeBetween(startTime, endTime)
                .stream()
                .collect(Collectors.groupingBy(BatchTeamSales::getTeamName));

        List<SalesHQTotalTeamPriceResDto> dtoList = new LinkedList<>();

        for(var teamSales : collect.keySet()) {
            BigDecimal totalPrice = collect.get(teamSales)
                    .stream()
                    .map(x -> new BigDecimal(x.getContractPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long count = collect.get(teamSales)
                    .stream()
                    .mapToLong(x -> Long.parseLong(x.getContractCount()))
                    .reduce(0L, Long::sum);

            BigDecimal cancelPrice = collect.get(teamSales)
                    .stream()
                    .map(x -> new BigDecimal(x.getCancelPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long cancelCount = collect.get(teamSales)
                    .stream()
                    .mapToLong(x -> Long.parseLong(x.getCancelCount()))
                    .reduce(0L, Long::sum);

            SalesHQTotalTeamPriceResDto teamPriceResDto = SalesHQTotalTeamPriceResDto.builder()
                    .teamName(teamSales)
                    .contractPrice(totalPrice.toEngineeringString())
                    .contractCount(String.valueOf(count))
                    .cancelPrice(cancelPrice.toEngineeringString())
                    .cancelCount(String.valueOf(cancelCount))
                    .build();

            dtoList.add(teamPriceResDto);
        }

        return dtoList;
    }

}
