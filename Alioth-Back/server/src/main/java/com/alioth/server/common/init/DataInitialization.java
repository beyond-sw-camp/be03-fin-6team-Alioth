package com.alioth.server.common.init;

import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.member.service.SalesMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final SalesMemberService salesMemberService;
    private final SalesMemberRepository salesMemberRepository;


    @Override
    public void run(String... args) throws Exception {
        List<SalesMembers> memberList = salesMemberRepository.findAll();

        if(memberList.size() == 0) {
            SalesMemberCreateReqDto fpDto = SalesMemberCreateReqDto.builder()
                    .email("jangdg@naver.com") // 마스킹
                    .phone("010-1234-0001") // 끝 4자리 마스킹
                    .name("장동건")
                    .password("a1234567!")
                    .birthDay("990123") // 마스킹
                    .address("장동건네집") // 마스킹
                    .rank(SalesMemberType.FP)
                    .build();

            SalesMemberCreateReqDto managerDto = SalesMemberCreateReqDto.builder()
                    .email("wonb@naver.com") // 마스킹
                    .phone("010-1234-0002") // 끝 4자리 마스킹
                    .name("원빈")
                    .password("a1234567!")
                    .birthDay("981101") // 마스킹
                    .address("원빈네집") // 마스킹
                    .rank(SalesMemberType.MANAGER)
                    .build();

            SalesMemberCreateReqDto hqDto = SalesMemberCreateReqDto.builder()
                    .email("gosoo@naver.com") // 마스킹
                    .phone("010-1234-0003") // 끝 4자리 마스킹
                    .name("고수")
                    .password("a1234567!")
                    .birthDay("970410") // 마스킹
                    .address("고수네집") // 마스킹
                    .rank(SalesMemberType.HQ)
                    .build();

            SalesMembers createMember1 = salesMemberService.create(fpDto);
            SalesMembers createMember2 = salesMemberService.create(managerDto);
            SalesMembers createMember3 = salesMemberService.create(hqDto);

            log.info("==========초기 데이터 생성==========");
            log.info("================================");
            log.info("이름: " + createMember1.getName());
            log.info("사번: " + createMember1.getSalesMemberCode());
            log.info("직급: " + createMember1.getRank());
            log.info("================================");
            log.info("이름: " + createMember2.getName());
            log.info("사번: " + createMember2.getSalesMemberCode());
            log.info("직급: " + createMember2.getRank());
            log.info("================================");
            log.info("이름: " + createMember3.getName());
            log.info("사번: " + createMember3.getSalesMemberCode());
            log.info("직급: " + createMember3.getRank());
            log.info("================================");
        }

    }
}
