package com.alioth.server.domain.contract.dto.req;


import com.alioth.server.domain.dummy.domain.ContractStatus;
import com.alioth.server.domain.dummy.domain.PaymentFrequency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.time.LocalDateTime;
@Builder
public record ContractCreateDto(
        @NotNull(message = "계약날짜가 필요합니다.")
        LocalDateTime contractDate,
        @NotNull(message = "계약만료일이 필요합니다.")
        LocalDateTime contractExpireDate,
        @NotBlank(message = "계약기간이 필요합니다.")
        String contractPeriod,
        @NotBlank(message = "계약 총 금액이 필요합니다.")
        String contractTotalPrice,
        @NotBlank(message = "납입금액이 필요합니다.")
        String contractPaymentAmount,
        @Valid
        PaymentFrequency contractPaymentFrequency,
        @NotNull(message = "납입 만기회차가 필요합니다.")
        Long contractPaymentMaturityInstallment,
        @NotNull(message = "납입회차가 필요합니다.")
        Long contractCount,
        @NotBlank(message = "납입방식이 필요합니다.")
        String contractPaymentMethod,
        @NotBlank(message = "납입자 정보가 필요합니다.")
        String contractPayer,
        @NotBlank(message = "계약 상담 내용이 필요합니다.")
        String contractConsultation,
        @Valid
        ContractStatus contractStatus,
        @NotNull(message = "보험 상품 ID를 입력해주세요.")
        Long insuranceProductId,
        @NotNull(message = "고객 ID를 입력해주세요.")
        Long customId,
        @NotNull(message = "계약 사원 ID를 입력해주세요.")
        Long contractMemberId
) {}
