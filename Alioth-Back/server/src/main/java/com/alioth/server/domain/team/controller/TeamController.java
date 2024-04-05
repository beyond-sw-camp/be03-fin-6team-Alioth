package com.alioth.server.domain.team.controller;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SMTeamListResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamReqDto;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final SalesMemberService salesMemberService;
    private final TypeChange typeChange;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createTeam(@RequestBody TeamReqDto dto) {
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
        if (teamManager.getRank() == SalesMemberType.MANAGER) {
            Team team = teamService.createTeam(dto, teamManager);
            salesMemberService.updateTeam(teamManager.getId(), team);
            List<SMTeamListResDto> list = team.getTeamMembers().stream().map(typeChange::smToSmTeamListResDto).toList();
            return CommonResponse.responseMessage(
                    HttpStatus.CREATED,
                    "successfully created",
                    typeChange.teamToTeamReqDto(team,list)
            );
        } else {
            throw new IllegalArgumentException("직급을 확인해주세요");
        }
    }

    @PatchMapping("/update/{teamId}")
    public ResponseEntity<CommonResponse> updateTeam(@RequestBody @Valid TeamReqDto dto,
                                        @PathVariable("teamId") Long id) {
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
        if (teamManager.getRank() == SalesMemberType.MANAGER) {
            teamService.updateTeam(dto, id);
            return CommonResponse.responseMessage(
                    HttpStatus.CREATED, "successfully updated"
            );
        } else {
            throw new IllegalArgumentException("직급을 확인해주세요");
        }
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<CommonResponse> deleteTeam(@PathVariable("teamId") Long id) {
        teamService.deleteTeam(id);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "successfully deleted"
        );
    }

    @GetMapping("/detail/{teamId}")
    public ResponseEntity<CommonResponse> teamDetail(@PathVariable("teamId") Long id) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "successfully loaded",
                teamService.findByTeamId(id)
        );
    }

    //팀원 추가
    @PostMapping("/addMembers/{teamId}")
    public ResponseEntity<CommonResponse> addMembers(@RequestBody List<Long> salesMemeberList,
                                                     @PathVariable("teamId") Long teamId) {
        List<SalesMembers> teamMembers = new ArrayList<>();
        for(Long salesMemberCode: salesMemeberList){
            SalesMembers member = salesMemberService.findBySalesMemberCode(salesMemberCode);
            if(member.getQuit().equals("N")){
                salesMemberService.updateTeam(member.getId(),teamService.findById(teamId));
                teamMembers.add(member);
            }
        }
        teamService.addMembersToTeam(teamId,teamMembers);
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "successfully added"
        );
    }
}
