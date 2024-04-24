package com.alioth.statistics.service.stat;


import com.alioth.statistics.domain.batch.dto.res.BatchHQSalesResDto;
import com.alioth.statistics.domain.batch.repository.BatchHQSalesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatHQSalesService {

    private final BatchHQSalesRepository hqSalesRepository;
    private LocalDateTime now;
    private int year;
    private int month;
    private int day;

    @PostConstruct
    public void init() {
        now = LocalDateTime.now();
        year = now.getYear();
        month = now.getMonthValue();
        day = now.getDayOfMonth();
    }

    public List<BatchHQSalesResDto> hqSalesDay() {
        LocalDateTime endTime = LocalDateTime.of(year, month, day, 0,0);
        LocalDateTime startTime = endTime.minusDays(1L);

        return getHQSales(startTime, endTime);
    }

    public List<BatchHQSalesResDto> hqSalesMonth() {
        YearMonth yearMonth = YearMonth.from(LocalDate.now());
        LocalDate dateOfMonth = yearMonth.atEndOfMonth();
        int dayOfMonth = dateOfMonth.getDayOfMonth();

        LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(year, month, dayOfMonth, 23, 59, 59);

        return getHQSales(startTime, endTime);
    }


    public List<BatchHQSalesResDto> hqSalesQuarter() {
        LocalDateTime startTime;
        LocalDateTime endTime;

        if(month > 6) {
            startTime = LocalDateTime.of(year, Month.JULY, 1, 0, 0, 0);
            endTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);
        }else {
            startTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0, 0);
            endTime = LocalDateTime.of(year, Month.JUNE, 30, 23, 59, 59);
        }

        return getHQSales(startTime, endTime);
    }

    public List<BatchHQSalesResDto> hqSalesYear() {
        LocalDateTime startTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);

        return getHQSales(startTime, endTime);
    }



    private List<BatchHQSalesResDto> getHQSales(LocalDateTime startTime, LocalDateTime endTime) {

        List<BatchHQSalesResDto> dto = hqSalesRepository.findByCreatedDateBetween(startTime, endTime)
                .stream()
                .map(BatchHQSalesResDto::ofBatchHQSales)
                .toList();

        return dto;
    }



}
