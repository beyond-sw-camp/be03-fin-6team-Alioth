package com.alioth.server.domain.team.controller;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamReqDto;
import com.alioth.server.domain.team.dto.TeamResDto;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/server/api/team")
public class TeamController {

    private final TeamService teamService;
    private final SalesMemberService salesMemberService;
    private final TypeChange typeChange;

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createTeam(@RequestBody TeamReqDto dto,
                                                     @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        //HQ 만 팀 생성 가능
        if(this.loginUser(userDetails).getRank()==SalesMemberType.HQ){
            // 팀장이 있어야 팀 생성 가능
            SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
            if (teamManager.getRank() == SalesMemberType.MANAGER) {
                Team team = teamService.createTeam(dto, teamManager);
                salesMemberService.updateTeam(teamManager.getId(), team);
                List<SalesMemberResDto> list = team.getTeamMembers().stream().map(typeChange::smToSmResDto).toList();
                return CommonResponse.responseMessage(
                        HttpStatus.CREATED,
                        "팀이 성공적으로 생성됩니다."
                );
                //팀장에 MANAGER 직급 아닌 경우
            } else {
                throw new IllegalArgumentException("직급을 확인해주세요");
            }
            //HQ 직원이 아닌 경우 --> 필요한지
        } else {
            throw new AccessDeniedException("권한이 없습니다");
        }
    }

    //팀 목록
    @GetMapping("/list")
    public ResponseEntity<CommonResponse> getTeamList( @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (this.loginUser(userDetails).getRank() != SalesMemberType.FP) {
            List<TeamResDto> list = new ArrayList<>();
            for(Team t : teamService.findAll()) {
                String teamManagerName = salesMemberService.findBySalesMemberCode(t.getTeamManagerCode()).getName();
                list.add(typeChange.teamToTeamResDto(t,teamManagerName));
            }
            return CommonResponse.responseMessage(
                    HttpStatus.CREATED,
                    "팀 목록을 불러옵니다.",
                    list
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    //팀 정보 수정
    @PatchMapping("/update/{teamCode}")
    public ResponseEntity<CommonResponse> updateTeam(@RequestBody @Valid TeamReqDto dto,
                                                     @PathVariable("teamCode") String teamCode,
                                                     @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (this.loginUser(userDetails).getRank() != SalesMemberType.FP) {
            teamService.updateTeam(dto, teamCode);
            return CommonResponse.responseMessage(
                    HttpStatus.CREATED,
                    "변경되었습니다."
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    // 팀 삭제
    @DeleteMapping("/delete/{teamCode}")
    public ResponseEntity<CommonResponse> deleteTeam(@PathVariable("teamCode") String teamCode,
                                                     @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if(this.loginUser(userDetails).getRank()==SalesMemberType.HQ){
            salesMemberService.exitTeam(teamService.findTeamMembersByTeamCode(teamCode));
            teamService.deleteTeam(teamCode);
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "삭제되었습니다."
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    @GetMapping("/detail/{teamCode}")
    public ResponseEntity<CommonResponse> teamDetail(@PathVariable("teamCode") String teamCode,
                                                     @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if(this.loginUser(userDetails).getRank()!=SalesMemberType.FP){
            Team team = teamService.findByCode(teamCode);
            String teamManagerName = salesMemberService.findBySalesMemberCode(team.getTeamManagerCode()).getName();
            List<SalesMemberResDto> list = teamService.findAllByTeamCode(teamCode);
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "팀 상세정보를 성공적으로 조회했습니다.",
                    typeChange.teamToTeamResDto(team,teamManagerName,list)
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다");
        }
    }

    //팀원 추가
    @PostMapping("/addMembers/{teamCode}")
    public ResponseEntity<CommonResponse> addMembers(@RequestBody List<Long> salesMemeberList,
                                                     @PathVariable("teamCode") String teamCode,
                                                     @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if(this.loginUser(userDetails).getRank()!=SalesMemberType.FP) {
            List<SalesMembers> teamMembers = new ArrayList<>();
            for (Long salesMemberCode : salesMemeberList) {
                SalesMembers member = salesMemberService.findBySalesMemberCode(salesMemberCode);
                if (member.getQuit().equals("N")) {
                    salesMemberService.updateTeam(member.getId(), teamService.findByTeamCode(teamCode));
                    teamMembers.add(member);
                }
            }
            teamService.addMembersToTeam(teamCode, teamMembers);
            return CommonResponse.responseMessage(
                    HttpStatus.CREATED,
                    "추가되었습니다."
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    public SalesMembers loginUser(UserDetails userDetails){
        return salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
    }
}
