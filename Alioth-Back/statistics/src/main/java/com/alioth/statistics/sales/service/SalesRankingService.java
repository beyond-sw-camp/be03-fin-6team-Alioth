package com.alioth.statistics.sales.service;

import com.alioth.statistics.domain.batch.BatchRankMember;
import com.alioth.statistics.domain.batch.dto.res.BatchMemberSalesResDto;
import com.alioth.statistics.domain.batch.repository.BatchMemberSalesRepository;
import com.alioth.statistics.domain.batch.repository.BatchRankMemberRepository;
import com.alioth.statistics.sales.dto.res.SalesMemberRankResDto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesRankingService {

    private final BatchRankMemberRepository rankMemberRepository;
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


    public List<SalesMemberRankResDto> memberSalesMonth() {
//        YearMonth yearMonth = YearMonth.from(LocalDate.now());
//        LocalDate dateOfMonth = yearMonth.atEndOfMonth();
//        int dayOfMonth = dateOfMonth.getDayOfMonth();

        LocalDateTime startTime = LocalDateTime.of(year, month, day, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(year, month, day, 23, 59, 59);

        return getMemberSales(startTime, endTime);
    }

//    public List<BatchMemberSalesResDto> memberSalesMonth(Long memberCode) {
//        YearMonth yearMonth = YearMonth.from(LocalDate.now());
//        LocalDate dateOfMonth = yearMonth.atEndOfMonth();
//        int dayOfMonth = dateOfMonth.getDayOfMonth();
//
//        LocalDateTime endTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
//        LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0);
//
//        return getMemberSales(memberCode, startTime, endTime);
//    }


    public List<SalesMemberRankResDto> memberSalesMonth(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date, formatter);

        LocalDateTime startTime = LocalDateTime.of(year, temp.getMonth().getValue(), temp.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(year, temp.getMonth().getValue(), temp.getDayOfMonth(), 23, 59, 59);


        return getMemberSales(startTime, endTime);
    }


    private List<SalesMemberRankResDto> getMemberSales(LocalDateTime startTime, LocalDateTime endTime) {

        List<SalesMemberRankResDto> dto = rankMemberRepository.findByCreatedDateBetween(startTime, endTime)
                .stream()
                .map(SalesMemberRankResDto::ofSalesMemberRankResDto)
                .toList();

        return dto;
    }


//    private List<BatchMemberSalesResDto> getMemberSales(Long memberCode, LocalDateTime startTime, LocalDateTime endTime) {
//
//        List<BatchMemberSalesResDto> dto = rankMemberRepository.findByCreatedDateBetween(startTime, endTime)
//                .stream()
//                .map(BatchMemberSalesResDto::ofBatchMemberSales)
//                .toList();
//
//        return dto;
//    }


}
