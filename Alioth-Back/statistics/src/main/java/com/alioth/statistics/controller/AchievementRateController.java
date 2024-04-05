package com.alioth.statistics.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.service.achievementrate.CompanyAchievementRateService;
import com.alioth.statistics.service.achievementrate.SMAchievementRateService;
import com.alioth.statistics.service.achievementrate.TeamAchievementRateService;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/stat")
@RestController
@RequiredArgsConstructor
public class AchievementRateController {

    private final SMAchievementRateService smAchievementRateService;
    private final TeamAchievementRateService teamAchievementRateService;
    private final CompanyAchievementRateService companyAchievementRateService;

    @GetMapping("/rate/sm/percent")
    public ResponseEntity<?> smAchievementRatePercent() {
        Map<SalesMembers, String> result = smAchievementRateService.achievementRatePercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "개인 달성률 입니다.",
                result
        );
    }

    @GetMapping("/rate/sm/count")
    public ResponseEntity<?> smAchievementRateCount() {
        Map<SalesMembers, String> result = smAchievementRateService.achievementRateCount();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "개인 달성건 입니다.",
                result
        );
    }


    @GetMapping("/rate/team/percent")
    public ResponseEntity<?> teamAchievementRatePercent() {
        Map<Team, String> result = teamAchievementRateService.achievementRatePercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 달성률 입니다.",
                result
        );
    }

    @GetMapping("/rate/team/count")
    public ResponseEntity<?> teamAchievementRateCount() {
        Map<Team, String> result = teamAchievementRateService.achievementRatePercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "팀 달성건 입니다.",
                result
        );
    }


    @GetMapping("/rate/company/percent")
    public ResponseEntity<?> companyAchievementRatePercent() {
        String result = companyAchievementRateService.achievementRatePercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전체 달성률 입니다.",
                result
        );
    }


    @GetMapping("/rate/company/count")
    public ResponseEntity<?> companyAchievementRateCount() {
        String result = companyAchievementRateService.achievementRatePercent();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "전체 달성건 입니다.",
                result
        );
    }







}
