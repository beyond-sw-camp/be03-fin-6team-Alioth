package com.alioth.server.domain.contract.dto.res;

import com.alioth.server.domain.dummy.domain.ContractStatus;
import com.alioth.server.domain.dummy.domain.PaymentFrequency;
import com.alioth.server.domain.excel.domain.ExcelHeaders;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import jakarta.persistence.Column;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@ExcelHeaders
public record ContractExcelResDto(
        @Column(name = "계약 번호")
        Long contractId,
        @Column(name = "계약 코드")
        String contractCode,
        @Column(name = "계약 계약 날짜")
        LocalDateTime contractDate,
        @Column(name = "계약 만기 일자")
        LocalDateTime contractExpireDate,
        @Column(name = "계약 기간(월)")
        String contractPeriod,
        @Column(name = "계약 총 금액")
        String contractTotalPrice,
        @Column(name = "납입 금액")
        String contractPaymentAmount,
        @Column(name = "납입 주기")
        PaymentFrequency contractPaymentFrequency,
        @Column(name = "납입 만기 회차")
        Long contractPaymentMaturityInstallment,
        @Column(name = "납입 회차")
        Long contractCount,
        @Column(name = "납입 방법")
        String contractPaymentMethod,
        @Column(name = "납입자")
        String contractPayer,
        @Column(name = "계약 상담 내역")
        String contractConsultation,
        @Column(name = "계약 상태")
        ContractStatus contractStatus,
        @Column(name = "보험 상픔")
        String insuranceProductName,
        @Column(name = "고객 이름")
        String customName,
        @Column(name = "계약 담당 사원")
        String contractMemberName,
        @Column(name = "영업 사원 코드")
        Long salesMemberCode,
        @Column(name = "영업 사원 이름")
        String salesMemberName
) {}
