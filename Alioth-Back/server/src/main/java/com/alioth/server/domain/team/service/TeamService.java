package com.alioth.server.domain.team.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SMTeamListResDto;
import com.alioth.server.domain.team.dto.TeamReqDto;
import com.alioth.server.domain.team.repository.TeamRepository;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamResDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TypeChange typeChange;

    public Team findByTeamCode(String teamCode) {
        return teamRepository.findByTeamCode(teamCode).orElseThrow(() -> new EntityNotFoundException("팀을 찾을 수 없습니다."));
    }

    public Team findById(Long id) {
        return teamRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("팀을 찾을 수 없습니다."));
    }

    public String createTeamCode() {
        Team findFirstTeam = teamRepository.findFirstByOrderByIdDesc();
        long teamId;
        if (findFirstTeam == null) {
            teamId = 1L;
        } else {
            teamId = teamRepository.findFirstByOrderByIdDesc().getId() + 1;
        }
        String formattedTeamId = String.format("%03d", teamId);
        return "SALES" + formattedTeamId;
    }

    public Team createTeam(TeamReqDto dto, SalesMembers teamManager) {
        String teamCode = this.createTeamCode();
        Team team = typeChange.teamCreateDtoToTeam(dto, teamCode);
        teamRepository.save(team);
        team.getTeamMembers().add(teamManager);
        return teamRepository.save(team);
    }

    public void deleteTeam(Long id) {
        Team team = this.findById(id);
        team.deleteTeam();
    }

    public void updateTeam(TeamReqDto dto, Long teamId) {
        Team team = this.findById(teamId);
        team.update(dto);
    }

    //팀 상세 조회
    public TeamResDto findByTeamId(Long teamId) throws EntityNotFoundException {
        Team team = this.findById(teamId);
        List<SMTeamListResDto> list = this.findAllByTeamId(team.getId());
        return typeChange.teamToTeamReqDto(team, list);
    }

    //팀원 추가
    public void addMembersToTeam(Long teamId, List<SalesMembers> teamMembers) {
        Team team = this.findById(teamId);
        team.getTeamMembers().addAll(teamMembers);
        teamRepository.save(team);
    }

    //사원 리스트 생성
    public List<SMTeamListResDto> findAllByTeamId (Long teamId){
        List<SalesMembers> memberList = teamRepository.findSalesMembersByTeamId(teamId);
        List<SMTeamListResDto> list = new ArrayList<>();
        for (SalesMembers sm : memberList) {
            if (sm.getQuit().equals("N")) {
                SMTeamListResDto dto = typeChange.smToSmTeamListResDto(sm);
                list.add(dto);
            }
        }
        return list;
    }
}

