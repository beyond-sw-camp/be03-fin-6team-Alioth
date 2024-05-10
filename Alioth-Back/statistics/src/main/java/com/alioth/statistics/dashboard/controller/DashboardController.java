package com.alioth.statistics.dashboard.controller;


import com.alioth.statistics.common.response.CommonResponse;
import com.alioth.statistics.dashboard.dto.res.DashboardBestTeamResDto;
import com.alioth.statistics.dashboard.dto.res.DashboardBoardResDto;
import com.alioth.statistics.dashboard.dto.res.DashboardGodResDto;
import com.alioth.statistics.dashboard.service.DashboardBoardService;
import com.alioth.statistics.dashboard.service.DashboardGodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardGodService dashboardService;
    private final DashboardBoardService dashboardBoardService;

    @GetMapping("/statistics/api/dashboard/god")
    public ResponseEntity<CommonResponse> getSalesGod() {
        DashboardGodResDto dto = dashboardService.getSalesGod();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "보험의 신",
                dto
        );
    }

    @GetMapping("/statistics/api/dashboard/best-team")
    public ResponseEntity<CommonResponse> getBestTeam() {
        DashboardBestTeamResDto dto = dashboardService.getBestTeam();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "최우수 고과 팀",
                dto
        );
    }


    @GetMapping("/statistics/api/dashboard/board/sug")
    public ResponseEntity<CommonResponse> getBoardSug() {
        List<DashboardBoardResDto> dto = dashboardBoardService.getBoardSug();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "공지사항 미리보기",
                dto
        );
    }

    @GetMapping("/statistics/api/dashboard/board/ann")
    public ResponseEntity<CommonResponse> getBoardAnn() {
        List<DashboardBoardResDto> dto = dashboardBoardService.getBoardAnn();

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "건의사항 미리보기",
                dto
        );
    }



}
