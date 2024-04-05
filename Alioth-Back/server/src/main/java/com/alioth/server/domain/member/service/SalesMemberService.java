package com.alioth.server.domain.member.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.*;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        return salesMemberRepository.findById(memberId).orElseThrow(()->
                                                            new EntityNotFoundException("존재하지 않는 사원입니다."));
    }

    public SalesMembers findBySalesMemberCode(Long salesMemberCode){
        return salesMemberRepository.findBySalesMemberCode(salesMemberCode).orElseThrow(()->
                                                            new EntityNotFoundException("존재하지 않는 사원입니다."));
    }

    //관리자 사원 정보 수정(권한, 팀 소속, 고과평가)
    @Transactional
    public SalesMemberResDto adminMemberUpdate (Long memberId, SMAdminUpdateReqDto dto) {
        SalesMembers member = this.findById(memberId);
        Team team = teamService.getTeam(dto.teamCode());
        member.updateAdmin(dto, team);
        salesMemberRepository.save(member);
        return typeChange.smToSmResDto(member);
    }

    //사원 정보 조회
    @Transactional
    public SalesMemberResDto memberDetail(Long memberId) {
        SalesMembers salesMembers = salesMemberRepository.findById(memberId).orElseThrow(()->
                                                            new EntityNotFoundException("존재하지 않는 사원입니다."));
        return typeChange.smToSmResDto(salesMembers);
    }

    @Transactional
    public SalesMemberResDto updateMyInfo(Long memberId, SalesMemberUpdateReqDto dto){
        SalesMembers member = this.findById(memberId);
        member.updateMyInfo(dto);
        return typeChange.smToSmResDto(member);
    }

    public void updateTeam(Long memberId,Team team){
        SalesMembers member = this.findById(memberId);
        member.updateTeam(team);
        salesMemberRepository.save(member);
    }

}
