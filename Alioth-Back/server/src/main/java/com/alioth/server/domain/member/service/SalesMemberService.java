package com.alioth.server.domain.member.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.*;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.team.domain.Team;
import com.alioth.server.domain.team.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    // Use salesMemberCode instead of the ID to find the member
    @Transactional
    public SalesMembers updatePassword(SalesMemberUpdatePassword dto, Long salesMemberCode) {
        SalesMembers findMember = salesMemberRepository.findBySalesMemberCode(salesMemberCode)
                .orElseThrow(() -> new EntityNotFoundException("해당 계정을 찾을 수 없습니다."));
        String encodedPassword = passwordEncoder.encode(dto.password());
        findMember.updatePassword(encodedPassword);
        salesMemberRepository.save(findMember);

        return findMember;
    }



    public SalesMembers findById(Long memberId){
        return salesMemberRepository.findById(memberId).orElseThrow(()->
                                                            new EntityNotFoundException("존재하지 않는 사원입니다."));
    }

    public List<SalesMemberResDto> findAll(){
        List<SalesMembers> list = salesMemberRepository.findAll();
        List<SalesMemberResDto> newList = new ArrayList<>();
        for(SalesMembers sm : list){
            newList.add(typeChange.smToSmResDto(sm));
        }
        return  newList;
    }

    public SalesMembers findBySalesMemberCode(Long salesMemberCode){
        return salesMemberRepository.findBySalesMemberCode(salesMemberCode).orElseThrow(()->
                                                            new EntityNotFoundException("존재하지 않는 사원입니다."));
    }

    //관리자 사원 정보 수정(권한, 팀 소속, 고과평가)
    @Transactional
    public SalesMemberResDto adminMemberUpdate (Long salesMemberCode, SMAdminUpdateReqDto dto) {
        SalesMembers member = this.findBySalesMemberCode(salesMemberCode);
        Team team = teamService.findByTeamCode(dto.teamCode());
        member.updateAdmin(dto, team);
        salesMemberRepository.save(member);
        return typeChange.smToSmResDto(member);
    }

    //사원 정보 조회
    @Transactional
    public SalesMemberResDto memberDetail(Long salesMemberCode) {
        return typeChange.smToSmResDto(this.findBySalesMemberCode(salesMemberCode));
    }

    @Transactional
    public SalesMemberResDto updateMyInfo(Long salesMemberCode, SalesMemberUpdateReqDto dto){
        SalesMembers member = this.findBySalesMemberCode(salesMemberCode);
        member.updateMyInfo(dto);
        return typeChange.smToSmResDto(member);
    }

    @Transactional
    public void updateTeam(Long memberId,Team team){
        SalesMembers member = this.findById(memberId);
        member.updateTeam(team);
        salesMemberRepository.save(member);
    }

    @Transactional
    public List<SalesMemberResDto> getAllMembers(){
        return salesMemberRepository.findAll().stream()
                        .filter(salesMembers -> salesMembers.getQuit().equals("N"))
                        .map(typeChange::smToSmResDto).toList();
    }

    @Transactional
    public List<SalesMemberResDto> getAllFPMembers(){
        return salesMemberRepository.findAll().stream()
                        .filter(salesMembers -> salesMembers.getRank()== SalesMemberType.FP)
                        .filter(salesMembers -> salesMembers.getQuit().equals("N"))
                        .map(typeChange::smToSmResDto).toList();
    }

    @Transactional
    public List<SalesMemberResDto> getAllManagerMembers(){
        return salesMemberRepository.findAll().stream()
                .filter(salesMembers -> salesMembers.getRank()== SalesMemberType.MANAGER)
                .filter(salesMembers -> salesMembers.getQuit().equals("N"))
                .map(typeChange::smToSmResDto).toList();
    }

    @Transactional
    public void deleteMember(Long salesMemberCode){
        this.findBySalesMemberCode(salesMemberCode).deleteMember();
    }

    @Transactional
    public void exitTeam(List<SalesMembers> list) {
        for (SalesMembers salesMembers : list) {
            salesMembers.exitTeam();
        }
    }

    public boolean existsBySalesMemberCode(Long salesMemberCode) {
        return salesMemberRepository.existsBySalesMemberCode(salesMemberCode);
    }

    public List<SalesMembers> getAllMembersByTeam(Long teamId) {
        return salesMemberRepository.findAllByTeamId(teamId);
    }

}
