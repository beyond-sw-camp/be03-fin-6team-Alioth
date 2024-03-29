package com.alioth.server.domain.member.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.*;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.dto.res.SalesMemberTeamListResDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesMemberService {

    private final PasswordEncoder passwordEncoder;
    private final SalesMemberRepository salesMemberRepository;

    private final TeamService teamService;

    private final TypeChange typeChange;


    @Transactional
    public SalesMembers create(SalesMemberCreateReqDto dto) {
        Long salesMemberCode = createSalesMemberCode();
        String encodePassword = passwordEncoder.encode(dto.password());
        SalesMembers createMember = typeChange.salesMemberCreateReqDtoToSalesMembers(dto, salesMemberCode, encodePassword);

        salesMemberRepository.save(createMember);

        return createMember;
    }

    private Long createSalesMemberCode() {
        LocalDateTime date = LocalDateTime.now();
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());
        Long id;
        SalesMembers findFirstMember = salesMemberRepository.findFirstByOrderByIdDesc();

        if(findFirstMember == null) {
            id = 1L;
        }else {
            id = findFirstMember.getId() + 1;
        }

        Long MemberCode = Long.valueOf(year + month + id.toString());

        return MemberCode;
    }

    @Transactional
    public SalesMembers updatePassword(SalesMemberUpdatePassword dto, Long id) {
        SalesMembers findMember = salesMemberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 계정을 찾을 수 없습니다."));
        findMember.updatePassword(dto.password());
        salesMemberRepository.save(findMember);

        return findMember;
    }


    public SalesMembers findById(Long memberId){
        return salesMemberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
    }

    public SalesMembers findBySalesMemberCode(Long salesMemberCode){
        return salesMemberRepository.findBySalesMemberCode(salesMemberCode).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 사원입니다."));
    }

    //관리자 사원 정보 수정(권한, 팀 소속) : HJ
    @Transactional
    public SalesMemberTeamListResDto adminMemberUpdate (Long memberId, SalesMemberAdminUpdateReqDto dto) {
        SalesMembers member = this.findById(memberId);
        Team team = teamService.getTeam(dto.teamCode());
        member.updateAdmin(dto, team);

        return SalesMemberTeamListResDto.builder()
                .rank(member.getRank())
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .salesMemberCode(member.getSalesMemberCode())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }
    //관리자 고과정보 수정
    @Transactional
    public void adminMemberPr (Long memberId, SalesMemberUpdatePerformanceReview dto){
        SalesMembers member = this.findById(memberId);
        member.updatePr(dto);
        salesMemberRepository.save(member);
    }

    //사원 정보 조회
    @Transactional
    public SalesMemberResDto memberDetail(Long memberId) {
        SalesMembers salesMembers = salesMemberRepository.findById(memberId).orElseThrow(()->
                                                            new EntityNotFoundException("존재하지 않는 사원입니다."));
        return SalesMemberResDto.builder()
                .rank(salesMembers.getRank())
                .salesMemberCode(salesMembers.getSalesMemberCode())
                .birthDay(salesMembers.getBirthDay())
                .performanceReview(salesMembers.getPerformanceReview())
                .teamCode(salesMembers.getTeam().getTeamCode())
                .teamName(salesMembers.getTeam().getTeamName())
                .address(salesMembers.getAddress())
                .officeAddress(salesMembers.getOfficeAddress())
                .extensionNumber(salesMembers.getExtensionNumber())
                .phone(salesMembers.getPhone())
                .name(salesMembers.getName())
                .email(salesMembers.getEmail())
                .build();
    }

    public SalesMemberResDto updateMyInfo(Long memberId, SalesMemberUpdateReqDto dto){
        SalesMembers member = this.findById(memberId);
        member.updateMyInfo(dto);
        salesMemberRepository.save(member);
        return SalesMemberResDto.builder()
                .rank(member.getRank())
                .salesMemberCode(member.getSalesMemberCode())
                .birthDay(member.getBirthDay())
                .teamCode(member.getTeam().getTeamCode())
                .teamName(member.getTeam().getTeamName())
                .address(member.getAddress())
                .officeAddress(member.getOfficeAddress())
                .extensionNumber(member.getExtensionNumber())
                .phone(member.getPhone())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
/*
    //사원 리스트
    public List<SalesMemberTeamListResDto> findAllByTeamId(Long teamId){
        List<SalesMembers> memberList= salesMemberRepository.findAllByTeamId(teamId);
        List<SalesMemberTeamListResDto> list = new ArrayList<>();
        for(SalesMembers sm: memberList){
            SalesMemberTeamListResDto dto= SalesMemberTeamListResDto.builder()
                    .name(sm.getName())
                    .rank(sm.getRank())
                    .profileImage(sm.getProfileImage())
                    .build();

           list.add(dto);
        }
        return list;
    }*/

    public void updateTeam(Long memberId,Team team){
        SalesMembers member= this.findById(memberId);
        member.updateTeam(team);
        salesMemberRepository.save(member);
    }

}
