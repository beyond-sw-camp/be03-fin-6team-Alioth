package com.alioth.server.domain.team.service;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.res.SalesMemberTeamListResDto;
import com.alioth.server.domain.team.dto.TeamDto;
import com.alioth.server.domain.team.dto.TeamUpdateDto;
import com.alioth.server.domain.team.repository.TeamRepository;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.dto.TeamCreateDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TeamService {
    private final TeamRepository teamRepository;
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeam(String teamCode){
        return teamRepository.findByTeamCode(teamCode);
    }
    public Team findById(Long id){
        return teamRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public String createTeamCode(){
        Team findFirstTeam = teamRepository.findFirstByOrderByIdDesc();
        Long teamId;
        if(findFirstTeam==null){
            teamId=1L;
        } else {
            teamId = teamRepository.findFirstByOrderByIdDesc().getId()+1;
        }
        String formattedTeamId = String.format("%03d", teamId);
        return "SALES"+ formattedTeamId;
    }

    public Team createTeam(TeamCreateDto dto, SalesMembers teamManager){
        Team team= Team.builder()
                .teamCode(this.createTeamCode())
                .teamName(dto.teamName())
                .teamManagerCode(dto.teamManagerCode())
                .build();
        teamRepository.save(team);
        team.getTeamMembers().add(teamManager);
        return teamRepository.save(team);
    }

    public void deleteTeam(Long id){
        Team team= this.findById(id);
        teamRepository.delete(team);
    }

    public void updateTeam(TeamUpdateDto dto, Long teamId){
        Team team = this.findById(teamId);
        team.update(dto);
        teamRepository.save(team);
    }

    //팀 상세 조회
    public TeamDto findByTeamId(Long teamId) throws EntityNotFoundException {
        Team team = this.findById(teamId);
        List<SalesMemberTeamListResDto> list= this.findAllByTeamId(team.getId());
        return TeamDto.builder()
                .teamCode(team.getTeamCode())
                .teamName(team.getTeamName())
                .teamManagerCode(team.getTeamManagerCode())
                .teamMemberList(list)
                .build();
    }

    //팀원 추가
    public void addMembersToTeam(Long teamId, List<SalesMembers> teamMembers){
        Team team = this.findById(teamId);
        for(SalesMembers sm: teamMembers){
            team.getTeamMembers().add(sm);
        }
    }
    //사원 리스트 가져오기
    public List<SalesMemberTeamListResDto> findAllByTeamId(Long teamId){
        List<SalesMembers> memberList= teamRepository.findSalesMembersByTeamId(teamId);
        List<SalesMemberTeamListResDto> list = new ArrayList<>();
        for(SalesMembers sm: memberList){
            SalesMemberTeamListResDto dto= SalesMemberTeamListResDto.builder()
                    .name(sm.getName())
                    .phone(sm.getPhone())
                    .email(sm.getEmail())
                    .address(sm.getAddress())
                    .officeAddress(sm.getOfficeAddress())
                    .extensionNumber(sm.getExtensionNumber())
                    .rank(sm.getRank())
                    .salesMemberCode(sm.getSalesMemberCode())
                    .profileImage(sm.getProfileImage())
                    .build();
            list.add(dto);
        }
        return list;
    }

}
