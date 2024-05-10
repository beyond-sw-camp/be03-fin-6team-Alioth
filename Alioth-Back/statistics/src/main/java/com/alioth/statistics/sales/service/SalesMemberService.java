package com.alioth.statistics.sales.service;

import com.alioth.statistics.domain.batch.BatchMemberSales;
import com.alioth.statistics.domain.batch.repository.BatchMemberSalesRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.target.sm.domain.SMSalesTarget;
import com.alioth.statistics.domain.target.sm.repository.SMSalesTargetRepository;
import com.alioth.statistics.sales.dto.res.SalesMemberMonthResDto;
import com.alioth.statistics.sales.dto.res.SalesMemberTargetResDto;
import com.alioth.statistics.sales.dto.res.SalesMemberTotalPriceRedDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalesMemberService {

    private final BatchMemberSalesRepository memberSalesRepository;
    private final SalesMemberRepository salesMemberRepository;
    private final SMSalesTargetRepository smTargetRepository;

    public List<SalesMemberMonthResDto> memberSalesMonth(Long memberCode, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);

        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        List<SalesMemberMonthResDto> dto = memberSalesRepository.findBySalesMemberCodeAndCreatedTimeBetween(memberCode, startTime, endTime)
                .stream()
                .map(SalesMemberMonthResDto::of)
                .toList();

        return dto;
    }

    public SalesMemberTotalPriceRedDto memberSalesPrice(Long memberCode, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);

        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        List<BatchMemberSales> dto = memberSalesRepository.findBySalesMemberCodeAndCreatedTimeBetween(memberCode, startTime, endTime);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Long count = 0L;
        BigDecimal totalCancelPrice = BigDecimal.ZERO;
        Long cancelCount = 0L;

        for(var member : dto) {
            totalPrice = totalPrice.add(new BigDecimal(member.getContractPrice()));
            count += Long.valueOf(member.getContractCount());
            totalCancelPrice = totalCancelPrice.add(new BigDecimal(member.getCancelPrice()));
            cancelCount += Long.valueOf(member.getCancelCount());
        }

        return SalesMemberTotalPriceRedDto.builder()
                .contractPrice(totalPrice.toEngineeringString())
                .contractCount(String.valueOf(count))
                .cancelPrice(totalCancelPrice.toEngineeringString())
                .cancelCount(String.valueOf(cancelCount))
                .build();
    }

    public Long salesMemberTarget(Long memberCode) {
        SalesMembers findMember = salesMemberRepository.findBySalesMemberCode(memberCode)
                .orElseThrow(() -> new EntityNotFoundException("사원 정보가 없습니다."));

        if(findMember.getMonthlyTargetPrice() == null || findMember.getMonthlyTargetPrice() == 0L) {
            return 0L;
        }

        return findMember.getMonthlyTargetPrice();
    }

    public Long salesMemberTarget(Long memberCode, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);

        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        SalesMembers findMember = salesMemberRepository.findBySalesMemberCode(memberCode)
                .orElseThrow(() -> new EntityNotFoundException("사원 정보가 없습니다."));

        SMSalesTarget smSalesTarget = smTargetRepository.findBySalesMembersAndTargetStartTimeBetween(findMember, startTime, endTime)
                .orElse(new SMSalesTarget());

        return smSalesTarget.getTargetPrice();
    }

    public Long memberSalesTargetResPrice(Long memberCode, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate temp = LocalDate.parse(date+"-01", formatter);
        YearMonth yearMonth = YearMonth.from(temp);

        LocalDateTime startTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(temp.getYear(), temp.getMonth().getValue(), yearMonth.atEndOfMonth().getDayOfMonth(), 23, 59, 59);

        List<BatchMemberSales> dto = memberSalesRepository.findBySalesMemberCodeAndCreatedTimeBetween(memberCode, startTime, endTime);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Long count = 0L;
        BigDecimal totalCancelPrice = BigDecimal.ZERO;
        Long cancelCount = 0L;

        for(var member : dto) {
            totalPrice = totalPrice.add(new BigDecimal(member.getContractPrice()));
        }

        return totalPrice.toBigInteger().longValue();
    }



}
