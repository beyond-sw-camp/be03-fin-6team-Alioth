package com.alioth.server.domain.team.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SalesMemberTeamListResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamAddMemberDto;
import com.alioth.server.domain.team.dto.TeamCreateDto;
import com.alioth.server.domain.team.dto.TeamDto;
import com.alioth.server.domain.team.dto.TeamUpdateDto;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final SalesMemberService salesMemberService;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createTeam(@RequestBody TeamCreateDto dto) {
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
        Team team = teamService.createTeam(dto,teamManager);
        salesMemberService.updateTeam(teamManager.getId(),team);
        TeamDto teamDto= TeamDto.builder()
                .teamName(team.getTeamName())
                .teamCode(team.getTeamCode())
                .teamMemberList(team.getTeamMembers().stream()
                        .map(a->
                                SalesMemberTeamListResDto.builder()
                                        .name(a.getName())
                                        .email(a.getEmail())
                                        .phone(a.getPhone())
                                        .extensionNumber(a.getExtensionNumber())
                                        .officeAddress(a.getOfficeAddress())
                                        .rank(a.getRank())
                                        .address(a.getAddress())
                                        .salesMemberCode(a.getSalesMemberCode())
                                        .profileImage(a.getProfileImage())
                                        .build()
                        ).collect(Collectors.toList()))
                .build();
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "successfully created",
                teamDto
        );
    }

    @PatchMapping("/update/{teamId}")
    public ResponseEntity<CommonResponse> updateTeam(@RequestBody @Valid TeamUpdateDto dto,
                                        @PathVariable("teamId") Long id) {
        teamService.updateTeam(dto,id);
        return CommonResponse.responseMessage(
                HttpStatus.CREATED, "successfully updated"
        );
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

    //팀 상세 정보 조회
    @GetMapping("/info/{teamId}")
    public ResponseEntity<CommonResponse> teamInfo(@PathVariable("teamId") Long teamId ) {

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "successfully loaded",
                teamService.findById(teamId));
    }

    //팀원 추가
    @PostMapping("/addMembers/{teamId}")
    public ResponseEntity<CommonResponse> addMembers(@RequestBody TeamAddMemberDto dto,
                                                     @PathVariable("teamId") Long teamId) {
        List<SalesMembers> teamMembers = new ArrayList<>();
        for(Long salesMemberCode: dto.salesMemberCodes()){
            SalesMembers teamMember=salesMemberService.findBySalesMemberCode(salesMemberCode);
            salesMemberService.updateTeam(teamMember.getId(),teamService.findById(teamId));
            teamMembers.add(teamMember);
        }
        teamService.addMembersToTeam(teamId,teamMembers);
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "successfully added"
        );
    }
}
