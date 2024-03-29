package com.alioth.server.domain.member.service;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.login.service.LoginService;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.dto.req.SalesMemberUpdatePassword;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;


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
                .address("축신") // 마스킹
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
                .address("축신") // 마스킹
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




}