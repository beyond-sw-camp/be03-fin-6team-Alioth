package com.alioth.statistics.service.stat;

import com.alioth.statistics.domain.batch.dto.res.BatchMemberSalesResDto;
import com.alioth.statistics.domain.batch.dto.res.BatchTeamSalesResDto;
import com.alioth.statistics.domain.batch.repository.BatchMemberSalesRepository;
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
@RequiredArgsConstructor
@Transactional
public class StatMemberSalesService {

    private final BatchMemberSalesRepository memberSalesRepository;
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

    public List<BatchMemberSalesResDto> memberSalesDay() {
        YearMonth yearMonth = YearMonth.from(LocalDate.now());
        LocalDate dateOfMonth = yearMonth.atEndOfMonth();
        int dayOfMonth = dateOfMonth.getDayOfMonth();

        LocalDateTime endTime = LocalDateTime.of(year, month, 11, 0, 0);
        LocalDateTime startTime = endTime.minusMonths(1L);

        return getMemberSales(startTime, endTime);
    }


    public List<BatchMemberSalesResDto> memberSalesMonth() {
        YearMonth yearMonth = YearMonth.from(LocalDate.now());
        LocalDate dateOfMonth = yearMonth.atEndOfMonth();
        int dayOfMonth = dateOfMonth.getDayOfMonth();

        LocalDateTime endTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
        LocalDateTime startTime = endTime.minusMonths(1L);

        return getMemberSales(startTime, endTime);
    }


    public List<BatchMemberSalesResDto> memberSalesQuarter() {
        LocalDateTime startTime;
        LocalDateTime endTime;

        if(month > 6) {
            startTime = LocalDateTime.of(year, Month.JULY, 1, 0, 0, 0);
            endTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);
        }else {
            startTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0, 0);
            endTime = LocalDateTime.of(year, Month.JUNE, 30, 23, 59, 59);
        }

        return getMemberSales(startTime, endTime);
    }

    public List<BatchMemberSalesResDto> memberSalesYear() {
        LocalDateTime startTime = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59);

        return getMemberSales(startTime, endTime);
    }


    private List<BatchMemberSalesResDto> getMemberSales(LocalDateTime startTime, LocalDateTime endTime) {

        List<BatchMemberSalesResDto> dto = memberSalesRepository.findByCreatedTimeBetween(startTime, endTime)
                .stream()
                .map(BatchMemberSalesResDto::ofBatchMemberSales)
                .toList();

        return dto;
    }


}
