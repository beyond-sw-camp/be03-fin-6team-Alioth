package com.alioth.statistics.service.stat;


import com.alioth.statistics.domain.batch.dto.res.BatchTeamSalesResDto;
import com.alioth.statistics.domain.batch.repository.BatchTeamSalesRepository;
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
public class StatTeamSalesService {

    private final BatchTeamSalesRepository teamSalesRepository;
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

    public List<BatchTeamSalesResDto> teamSalesDay() {
        LocalDateTime endTime = LocalDateTime.of(year, month, 9, 0, 0);
        LocalDateTime startTime = endTime.minusDays(1L);

        return getTeamSales(startTime, endTime);
    }

    public List<BatchTeamSalesResDto> teamSalesMonth() {
        YearMonth yearMonth = YearMonth.from(LocalDate.now());
        LocalDate dateOfMonth = yearMonth.atEndOfMonth();
        int dayOfMonth = dateOfMonth.getDayOfMonth();

        LocalDateTime endTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
        LocalDateTime startTime = endTime.minusMonths(1L);

        return getTeamSales(startTime, endTime);
    }


    public List<BatchTeamSalesResDto> teamSalesQuarter() {
        LocalDateTime startTime;
        LocalDateTime endTime;

        if(month > 6) {
            startTime = LocalDateTime.of(year, Month.JULY, 1, 0, 0, 0);
            endTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);
        }else {
            startTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0, 0);
            endTime = LocalDateTime.of(year, Month.JUNE, 30, 23, 59, 59);
        }

        return getTeamSales(startTime, endTime);
    }

    public List<BatchTeamSalesResDto> teamSalesYear() {
        LocalDateTime startTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);

        return getTeamSales(startTime, endTime);
    }



    private List<BatchTeamSalesResDto> getTeamSales(LocalDateTime startTime, LocalDateTime endTime) {

        List<BatchTeamSalesResDto> dto = teamSalesRepository.findByCreatedTimeBetween(startTime, endTime)
                .stream()
                .map(BatchTeamSalesResDto::ofBatchTeamSales)
                .toList();

        return dto;
    }



}
