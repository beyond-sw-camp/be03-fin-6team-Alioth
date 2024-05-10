package com.alioth.server.domain.member.service;

import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.login.service.LoginService;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.*;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Rollback
@Transactional
@SpringBootTest
class SalesMemberServiceTest {


    @Autowired
    private SalesMemberService salesMemberService;

    @Autowired
    private SalesMemberRepository salesMemberRepository;

    @Autowired
    private LoginService loginService;

    @Test
    @DisplayName("맴버등록하기")
    public void 맴버등록테스트() {
        SalesMemberCreateReqDto dto = SalesMemberCreateReqDto.builder()
                .email("sj@naver.com") // 마스킹
                .phone("010-1234-1234") // 끝 4자리 마스킹
                .name("손흥민")
                .password("a1234567")
                .birthDay("990123") // 마스킹
                .roadAddress("축신") // 마스킹
                .rank(SalesMemberType.FP)
                .build();

        SalesMembers createMember = salesMemberService.create(dto);
        SalesMembers findMember = salesMemberRepository.findById(createMember.getId()).orElse(null);

        Assertions.assertThat(createMember.getSalesMemberCode()).isEqualTo(findMember.getSalesMemberCode());
    }

    @Test
    @DisplayName("회원가입 후 시큐리티에서 확인")
    public void 회원가입후시큐리티확인() {
        SalesMemberCreateReqDto dto = SalesMemberCreateReqDto.builder()
                .email("qqwe@naver.com") // 마스킹
                .phone("010-1234-1234") // 끝 4자리 마스킹
                .name("주민규")
                .password("aA@1234567")
                .birthDay("990123") // 마스킹
                .roadAddress("축신") // 마스킹
                .rank(SalesMemberType.FP)
                .build();

        SalesMembers createMember = salesMemberService.create(dto);

        LoginReqDto reqDto = LoginReqDto.builder()
                .memberCode(createMember.getSalesMemberCode())
                .password("aA@1234567")
                .build();

        LoginResDto loginResDto = loginService.memberLogin(reqDto);

        String name = SecurityContextHolder.getContext().getAuthentication().getName();


    }

    @Test
    @DisplayName("비밀번호변경 테스트")
    public void 비밀번호변경확인() {
        SalesMemberUpdatePassword passDto = SalesMemberUpdatePassword.builder().password("12389asdj@1!@").build();
        Long id = 7L;

        SalesMembers members = salesMemberService.updatePassword(passDto, id);
        salesMemberRepository.save(members);
        Assertions.assertThat(passDto.password()).isEqualTo(members.getPassword());
    }

    @Test
    @DisplayName("관리자 사원 정보(소속 변경, 직급, 고과평가) 수정")
    public void adminMemberUpdateTest(){
        SMAdminUpdateReqDto dto = SMAdminUpdateReqDto.builder()
                .teamCode("SALES005")
                .rank(SalesMemberType.FP)
                .performanceReview("A")
                .build();
        Long id = 6L;

        SalesMemberResDto member = salesMemberService.adminMemberUpdate(id,dto);
        Assertions.assertThat(dto.rank()).isEqualTo(member.rank());
        assertEquals("A", member.performanceReview());
    }


    @Test
    @DisplayName("사원 정보 조회")
    public void memberDetailTest(){
        Long id = 4L;
        SalesMemberResDto member = salesMemberService.memberDetail(id);
        assertEquals(202434,member.salesMemberCode());
    }

    @Test
    @DisplayName("내 정보 수정")
    public void updateMyInfoTest(){
        Long id = 4L;
        SalesMemberUpdateReqDto dto = SalesMemberUpdateReqDto.builder()
                .birthDay("1998-12-03")
                .phone("010-8556-4451")
                .email("asepa@gmail.com")
                .roadAddress("서울특별시 압구정구")
                .extensionNumber("02-6642-8789")
                .officeAddress("10층 1002호")
                .build();

        SalesMemberResDto member = salesMemberService.updateMyInfo(id,dto);
        assertEquals("1998-12-03",member.birthDay());
    }
}