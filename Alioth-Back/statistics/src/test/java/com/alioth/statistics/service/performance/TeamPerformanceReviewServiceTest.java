package com.alioth.statistics.service.performance;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.dto.res.TeamPerformanceReviewResDto;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@SpringBootTest
class TeamPerformanceReviewServiceTest {

    @Autowired private TeamRepository teamRepository;
    @Autowired private ContractRepository contractRepository;
    private final Map<String, Long> scoreMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        scoreMap.put("A", 10L);
        scoreMap.put("B", 7L);
        scoreMap.put("C", 5L);
    }


    @Test
    @DisplayName("고과 평가 최우수 지점")
    @Transactional
    public void 고과평가최우수지점() {
//        List<Contract> contractList = contractRepository.findAll();
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

        dto.sort(new Comparator<TeamPerformanceReviewResDto>() {
            @Override
            public int compare(TeamPerformanceReviewResDto o1, TeamPerformanceReviewResDto o2) {
                int result = (int) (Long.valueOf(o2.teamPerformanceReview()) - Long.valueOf(o1.teamPerformanceReview()));

                if(result == 0) {
                    result = (int) (Long.valueOf(o2.teamTotal()) - Long.valueOf(o1.teamTotal()));
                }

                if(result == 0) {
                    result = (int) (Long.valueOf(o2.teamCount()) - Long.valueOf(o1.teamCount()));
                }

                return result;
            }
        });


        System.out.println("dto = " + dto);
    }


    private Long performanceScore(String review) {
        return scoreMap.get(review);
    }


}
