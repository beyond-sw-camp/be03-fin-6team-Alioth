package com.alioth.server.domain.contract.dto.res;

import com.alioth.server.domain.dummy.domain.ContractStatus;
import com.alioth.server.domain.dummy.domain.PaymentFrequency;
import com.alioth.server.domain.excel.domain.ExcelHeaders;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@ExcelHeaders
public record ContractResDto(
        Long contractId,
        String contractCode,
        LocalDateTime contractDate,
        LocalDateTime contractExpireDate,
        String contractPeriod,
        String contractTotalPrice,
        String contractPaymentAmount,
        PaymentFrequency contractPaymentFrequency,
        Long contractPaymentMaturityInstallment,
        Long contractCount,
        String contractPaymentMethod,
        String contractPayer,
        String contractConsultation,
        ContractStatus contractStatus,
        String insuranceProductName,
        String customName,
        String contractMemberName,
        Long salesMemberCode,
        SalesMemberResDto salesMemberResDto,
        String salesMemberName
) {}
