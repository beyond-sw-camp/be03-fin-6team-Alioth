package com.alioth.statistics.service;

import com.alioth.statistics.common.config.jpa.JPAFactoryConfig;
import com.alioth.statistics.domain.board.domain.QBoard;
import com.alioth.statistics.domain.contract.domain.QContract;
import com.alioth.statistics.domain.dummy.repository.ContractMembersRepository;
import com.alioth.statistics.domain.dummy.repository.InsuranceProductRepository;
import com.alioth.statistics.domain.member.domain.QSalesMembers;
import com.alioth.statistics.domain.member.domain.SalesMembers;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRankServiceTest {

    @Autowired private InsuranceProductRepository insuranceProductRepository;
    @Autowired private SalesMemberRepository salesMemberRepository;
    @Autowired private ContractMembersRepository contractMembersRepository;
    @Autowired private JPAQueryFactory query;

    @Test
    @DisplayName("맴버 사원수 확인 테스트")
    @Transactional
    public void 맴버사원수확인테스트() {
        long count1 = insuranceProductRepository.count();
        long count2 = salesMemberRepository.count();
        long count3 = contractMembersRepository.count();

        Assertions.assertThat(count1).isEqualTo(1000L);
        Assertions.assertThat(count2).isEqualTo(3L);
        Assertions.assertThat(count3).isEqualTo(1000L);
    }



    @DisplayName("QueryDSL으로 테이블 정보 가져오기")
    public void 테이블가져오기() {
        QSalesMembers contract = QSalesMembers.salesMembers;


        //System.out.println("fetch = " + fetch);

    }


}