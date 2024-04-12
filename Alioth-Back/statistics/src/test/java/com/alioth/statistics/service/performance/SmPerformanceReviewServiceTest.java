package com.alioth.statistics.service.performance;

import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.dto.res.MemberPerformanceReviewResDto;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.alioth.statistics.domain.team.domain.Team;
import com.alioth.statistics.domain.team.repository.TeamRepository;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SmPerformanceReviewServiceTest {

    @Autowired private TeamRepository teamRepository;
    @Autowired private SalesMemberRepository salesMemberRepository;


    @Test
    @DisplayName("고과 평가 최우수 FP")
    public void 고과평가최우수FP() {
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

        System.out.println("dto = " + dto);
    }

    private String totalConvert(String price) {
        double scientificNotation = Double.parseDouble(price);
        BigInteger bigIntegerValue = BigDecimal.valueOf(scientificNotation).toBigInteger();
        return bigIntegerValue.toString();
    }

    @Test
    @DisplayName("고과 평가 최우수 지점")
    public void 고과평가최우수지점() {
        List<Team> teamList = teamRepository.findAll();

        for (var team : teamList) {


        }

    }


}