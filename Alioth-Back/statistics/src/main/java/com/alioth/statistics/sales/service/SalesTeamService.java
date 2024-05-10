package com.alioth.statistics.sales.service;


import com.alioth.statistics.domain.batch.BatchTeamSales;
import com.alioth.statistics.domain.batch.repository.BatchMemberSalesRepository;
import com.alioth.statistics.domain.batch.repository.BatchTeamSalesRepository;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import com.alioth.statistics.sales.dto.res.SalesTeamTargetResDto;
import com.alioth.statistics.sales.dto.res.SalesTeamTotalPriceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesTeamService {

    private final TeamRepository teamRepository;
    private final BatchMemberSalesRepository memberSalesRepository;
    private final BatchTeamSalesRepository teamSalesRepository;
    private final SalesMemberRepository salesMemberRepository;


    public SalesTeamTargetResDto teamTarget(String memberTeamCode, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);
        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        Team findTeam = teamRepository.findByTeamCode(memberTeamCode);

        List<BatchTeamSales> findTeamPrice = teamSalesRepository.findByTeamCodeAndCreatedTimeBetween(memberTeamCode, startTime, endTime);
        BigDecimal teamTotalPrice = findTeamPrice
                .stream()
                .map(x -> new BigDecimal(x.getContractPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return SalesTeamTargetResDto.builder()
                .targetPrice(findTeam.getMonthlyTargetPrice())
                .price(Long.valueOf(teamTotalPrice.toEngineeringString()))
                .build();
    }


    public SalesTeamTotalPriceResDto teamTotalPrice(String memberTeamCode, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);
        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        Map<String, List<BatchTeamSales>> collect = teamSalesRepository.findByTeamCodeAndCreatedTimeBetween(memberTeamCode, startTime, endTime)
                .stream()
                .collect(Collectors.groupingBy(BatchTeamSales::getTeamCode));

        List<BatchTeamSales> batchTeamSales = collect.get(memberTeamCode);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Long totalCount = 0L;
        BigDecimal totalCancelPrice = BigDecimal.ZERO;
        Long totalCancelCount = 0L;

        for (var batchTeamSale : batchTeamSales) {
            totalPrice = totalPrice.add(new BigDecimal(batchTeamSale.getContractPrice()));
            totalCount += Long.valueOf(batchTeamSale.getContractCount());
            totalCancelPrice = totalCancelPrice.add(new BigDecimal(batchTeamSale.getCancelPrice()));
            totalCancelCount += Long.valueOf(batchTeamSale.getCancelCount());
        }

        return SalesTeamTotalPriceResDto.builder()
                .contractPrice(totalPrice.toEngineeringString())
                .contractCount(String.valueOf(totalCount))
                .cancelPrice(totalCancelPrice.toEngineeringString())
                .cancelCount(String.valueOf(totalCancelCount))
                .build();
    }



}
