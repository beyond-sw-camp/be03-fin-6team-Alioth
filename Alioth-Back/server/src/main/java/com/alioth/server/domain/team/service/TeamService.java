package com.alioth.server.domain.team.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.team.dto.TeamReqDto;
import com.alioth.server.domain.team.repository.TeamRepository;
import com.alioth.server.domain.team.domain.Team;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
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

    public void deleteTeam(String teamCode) {
        Team team = this.findByTeamCode(teamCode);
        team.deleteTeam();
    }

    public void updateTeam(TeamReqDto dto, String teamCode) {
        Team team = this.findByTeamCode(teamCode);
        team.update(dto);
    }

    //팀 목록
    public List<Team> findAll() {
       return teamRepository.findAll().stream()
               .filter(team -> team.getDelYN().equals("N"))
               .toList();
    }

    //팀 상세 조회
    public Team findByCode(String teamCode) throws EntityNotFoundException {
      return this.findByTeamCode(teamCode);
    }

    //팀원 추가
    public void addMembersToTeam(String teamCode, List<SalesMembers> teamMembers) {
        Team team = this.findByTeamCode(teamCode);
        team.getTeamMembers().addAll(teamMembers);
        teamRepository.save(team);
    }


    //사원 리스트 생성
    public List<SalesMemberResDto> findAllByTeamCode (String teamCode){
        List<SalesMemberResDto> list = new ArrayList<>();
        for (SalesMembers sm : this.findTeamMembersByTeamCode(teamCode)) {
            if (sm.getQuit().equals("N")) {
                SalesMemberResDto dto = typeChange.smToSmResDto(sm);
                list.add(dto);
            }
        }
        return list;
    }

    public List<SalesMembers> findTeamMembersByTeamCode(String teamCode){
        return teamRepository.findSalesMembersByTeamCode(teamCode);
    }
}

