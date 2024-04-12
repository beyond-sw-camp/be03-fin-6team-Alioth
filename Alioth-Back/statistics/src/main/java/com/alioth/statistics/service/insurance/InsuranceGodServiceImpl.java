package com.alioth.statistics.service.insurance;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.ContractStatus;
import com.alioth.statistics.domain.insurance.dto.res.InsuranceGodResDto;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceGodServiceImpl implements InsuranceGodService{

    private final ContractRepository contractRepository;
    private final SalesMemberRepository salesMemberRepository;

    @Override
    public InsuranceGodResDto insuranceOfGodMonth() {
//        LocalDateTime startDate = LocalDateTime.of(2024, 3, 2, 0, 0);
//        LocalDateTime endDate = LocalDateTime.of(2024, 4, 2, 0, 0);
        Pair<LocalDateTime, LocalDateTime> dateTime = startAndEndDateMonth(1L);

        InsuranceGodResDto insuranceGodMonthResDto = insuranceGodResDto(dateTime.getFirst(), dateTime.getSecond());

        return insuranceGodMonthResDto;
    }

    @Override
    public InsuranceGodResDto insuranceOfGodQuarter() {
        Pair<LocalDateTime, LocalDateTime> dateTime = startAndEndDateMonth(6L);
        InsuranceGodResDto insuranceGodQuarterResDto = insuranceGodResDto(dateTime.getFirst(), dateTime.getSecond());

        return insuranceGodQuarterResDto;
    }

    @Override
    public InsuranceGodResDto insuranceOfGodYear() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusYears(1L);

        InsuranceGodResDto insuranceGodYearResDto = insuranceGodResDto(startDate, endDate);

        return insuranceGodYearResDto;
    }

    private Pair<LocalDateTime, LocalDateTime> startAndEndDateMonth(Long time) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int dayOfMonth = now.getDayOfMonth();
        LocalDateTime endDate = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
        LocalDateTime startDate = endDate.minusMonths(time);


        return Pair.of(startDate, endDate);
    }

    private InsuranceGodResDto insuranceGodResDto(LocalDateTime startDate, LocalDateTime endDate) {
        List<SalesMembers> memberList = salesMemberRepository.findAll();
        List<InsuranceGodResDto> dto = new LinkedList<>();

        for (var member : memberList) {
            List<Contract> memberContractList = contractRepository.findBySalesMembersAndContractDateBetween(member, startDate, endDate);

            if(memberContractList.size() == 0) {
                return InsuranceGodResDto.builder()
                        .memberCode(member.getSalesMemberCode().toString())
                        .memberName(member.getName())
                        .totalPrice(BigDecimal.ZERO.toPlainString())
                        .Count(0L)
                        .CancelAvgPrice(BigDecimal.ZERO.toPlainString())
                        .performanceReview(member.getPerformanceReview())
                        .build();
            }

            BigDecimal price = memberContractList.stream()
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            long count = memberContractList.stream().count();

            BigDecimal cancelTotalPrice = memberContractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            long cancelCount = memberContractList.stream()
                    .filter(contract -> contract.getContractStatus() == ContractStatus.Cancellation)
                    .count();

            String cancelAvgPrice = cancelTotalPrice.divide(BigDecimal.valueOf(cancelCount)).toPlainString();

            InsuranceGodResDto resDto = InsuranceGodResDto.builder()
                    .memberCode(member.getSalesMemberCode().toString())
                    .memberName(member.getName())
                    .totalPrice(price.toPlainString())
                    .Count(count)
                    .CancelAvgPrice(cancelAvgPrice)
                    .performanceReview(member.getPerformanceReview())
                    .build();

            dto.add(resDto);
        }

        dto.sort((o1, o2) -> {
            BigDecimal o2_divide = new BigDecimal(o2.totalPrice()).divide(BigDecimal.valueOf(o2.Count()));
            BigDecimal o2_cancelAvgPrice = new BigDecimal(o2.CancelAvgPrice());
            BigDecimal o2_subPrice = o2_divide.subtract(o2_cancelAvgPrice);

            BigDecimal o1_divide = new BigDecimal(o1.totalPrice()).divide(BigDecimal.valueOf(o1.Count()));
            BigDecimal o1_cancelAvgPrice = new BigDecimal(o1.CancelAvgPrice());
            BigDecimal o1_subPrice = o1_divide.subtract(o1_cancelAvgPrice);

            BigDecimal subtractResult = o2_subPrice.subtract(o1_subPrice);

            int result;
            if(subtractResult.compareTo(BigDecimal.ZERO) > 0) {
                result = 1;
            }else if(subtractResult.compareTo(BigDecimal.ZERO) < 0) {
                result = -1;
            }else {
                result = 0;
            }

            return result;
        });

        return dto.get(0);
    }


}
