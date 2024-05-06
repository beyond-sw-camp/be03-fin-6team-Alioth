package com.alioth.server.domain.team.service;

import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamResDto;
import com.alioth.server.domain.team.dto.TeamReqDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Rollback
@Transactional
@SpringBootTest
public class TeamServiceTest {


    @Autowired
    private TeamService teamService;

    @Autowired
    private SalesMemberService salesMemberService;

    @Test
    @DisplayName("팀 생성")
    public void createTeamTest() {
        String teamCode = teamService.createTeamCode();
        Long teamManagerCode = 202435L;
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(teamManagerCode);
        if (teamManager.getRank() == SalesMemberType.MANAGER) {
            TeamReqDto dto = TeamReqDto.builder()
                    .teamName(teamCode)
                    .teamManagerCode(teamManagerCode)
                    .build();
            Team team = teamService.createTeam(dto, teamManager);
            assertEquals(teamCode, team.getTeamCode());
        } else {
            throw new IllegalArgumentException("직급을 확인해주세요");
        }
    }

    @Test
    @DisplayName("팀 삭제")
    public void deleteTeamTest() {
        Long teamId = 2L;
        Team team = teamService.findById(teamId);
        team.deleteTeam();
        assertEquals("Y", team.getDelYN());
    }

    @Test
    @DisplayName("팀 정보 수정")
    public void updateTeamTest() {
        Long teamId = 1L;
        TeamReqDto dto = TeamReqDto.builder()
                .teamName("SALES1")
                .teamManagerCode(202435L)
                .build();
        Team team = teamService.findById(teamId);
        SalesMembers teamManager = salesMemberService.findBySalesMemberCode(dto.teamManagerCode());
        if (teamManager.getRank() == SalesMemberType.MANAGER) {
            teamService.updateTeam(dto, teamId);
        }
        assertEquals("SALES1", team.getTeamName());
        assertEquals(dto.teamManagerCode(), team.getTeamManagerCode());
    }


    @Test
    @DisplayName("팀 상세정보 조회")
    public void findByTeamIdTest() {
        Long teamId = 2L;
        TeamResDto dto = teamService.findByTeamCode(teamId);
        Team team = teamService.findById(teamId);
        assertEquals(team.getTeamCode(), dto.teamCode());
    }

    @Test
    @DisplayName("팀원 추가")
    public void addMembersToTeamTest() {
        Long teamId = 1L;
        List<Long> salesMembersCode = new ArrayList<>();
        salesMembersCode.add(202435L);
        salesMembersCode.add(202436L);
        salesMembersCode.add(202437L);
        List<SalesMembers> teamMembers = new ArrayList<>();

        for (Long smc : salesMembersCode) {
            teamMembers.add(salesMemberService.findBySalesMemberCode(smc));
        }
        teamService.addMembersToTeam(teamId, teamMembers);
        SalesMembers member1 = salesMemberService.findBySalesMemberCode(202435L);
        SalesMembers member2 = salesMemberService.findBySalesMemberCode(202437L);
        Team team = teamService.findById(teamId);
        List<SalesMembers> teamMembersList = team.getTeamMembers();

//        assertTrue(team.getTeamMembers().contains(member2));
        assertTrue(Objects.equals(team.getId(), member1.getTeam().getId()));
    }
}


