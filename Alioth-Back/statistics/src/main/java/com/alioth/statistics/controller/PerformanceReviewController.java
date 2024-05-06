package com.alioth.statistics.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.domain.member.dto.res.MemberPerformanceReviewResDto;
import com.alioth.statistics.domain.member.dto.res.TeamPerformanceReviewResDto;
import com.alioth.statistics.service.performance.PerformanceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/statistics")
@RestController
@RequiredArgsConstructor
public class PerformanceReviewController {

    private final PerformanceReviewService smPerformanceReviewService;


    @GetMapping("/performance/sm/")
    public ResponseEntity<CommonResponse> performanceReviewMember() {
        List<MemberPerformanceReviewResDto> result = smPerformanceReviewService.performanceReviewMember();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "고과 평가 사원 순위입니다.",
                result
        );
    }


    @GetMapping("/performance/team/")
    public ResponseEntity<CommonResponse> performanceReviewTeam() {
        List<TeamPerformanceReviewResDto> result = smPerformanceReviewService.performanceReviewTeam();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "고과 평가 팀 순위입니다.",
                result
        );
    }


}
