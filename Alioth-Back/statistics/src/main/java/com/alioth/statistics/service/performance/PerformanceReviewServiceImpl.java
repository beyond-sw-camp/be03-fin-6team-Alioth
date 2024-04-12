package com.alioth.statistics.service.performance;


import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.dto.res.MemberPerformanceReviewResDto;
import com.alioth.statistics.domain.member.dto.res.TeamPerformanceReviewResDto;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceReviewServiceImpl implements PerformanceReviewService{

    private final TeamRepository teamRepository;
    private final SalesMemberRepository salesMemberRepository;
    private final ContractRepository contractRepository;
    private final Map<String, Long> scoreMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        scoreMap.put("A", 10L);
        scoreMap.put("B", 7L);
        scoreMap.put("C", 5L);
    }



    @Override
    public List<MemberPerformanceReviewResDto> performanceReviewMember() {
        List<Map<String, Object>> maps = salesMemberRepository.memberPerformanceReviewRank();
        List<MemberPerformanceReviewResDto> dto = new LinkedList<>();

        for(var map : maps) {
            MemberPerformanceReviewResDto build = MemberPerformanceReviewResDto.builder()
                    .salesMemberCode(map.get("sales_member_code").toString())
                    .name(map.get("name").toString())
                    .performanceReview(map.get("performance_review").toString())
                    .totalCount(map.get("totalcount").toString())
                    .totalPrice(totalConvert(map.get("totalprice").toString()))
                    .build();
            dto.add(build);
        }

        return dto;
    }

    private String totalConvert(String price) {
        double scientificNotation = Double.parseDouble(price);
        BigInteger bigIntegerValue = BigDecimal.valueOf(scientificNotation).toBigInteger();
        return bigIntegerValue.toString();
    }


    @Override
    public List<TeamPerformanceReviewResDto> performanceReviewTeam() {
        List<Team> teamList = teamRepository.findAll();
        List<TeamPerformanceReviewResDto> dto = new LinkedList<>();

        for (var team : teamList) {
            List<SalesMembers> memberList = team.getTeamMembers();
            BigInteger priceSum = BigInteger.ZERO;
            Long countSum = 0L;
            Long performanceSum = 0L;

            for (var member : memberList) {
                performanceSum += performanceScore(member.getPerformanceReview());

                List<Contract> contractList = contractRepository.findBySalesMembers(member);

                BigInteger price = contractList.stream()
                        .map(x -> new BigInteger(x.getContractTotalPrice()))
                        .reduce(BigInteger::add)
                        .orElse(BigInteger.ZERO);
                priceSum = priceSum.add(price);

                long count = contractList.stream().count();
                countSum += count;
            }

            TeamPerformanceReviewResDto build = TeamPerformanceReviewResDto.builder()
                    .teamCode(team.getTeamCode())
                    .teamPerformanceReview(String.valueOf(performanceSum))
                    .teamTotal(priceSum.toString())
                    .teamCount(String.valueOf(countSum))
                    .build();

            dto.add(build);
        }

        dto.sort((o1, o2) -> {
            int result = (int) (Long.valueOf(o2.teamPerformanceReview()) - Long.valueOf(o1.teamPerformanceReview()));

            if(result == 0) {
                result = (int) (Long.valueOf(o2.teamTotal()) - Long.valueOf(o1.teamTotal()));
            }

            if(result == 0) {
                result = (int) (Long.valueOf(o2.teamCount()) - Long.valueOf(o1.teamCount()));
            }

            return result;
        });

        return dto;
    }


    private Long performanceScore(String review) {
        return scoreMap.get(review);
    }

}
