package com.alioth.server.domain.contract.dto.req;
import com.alioth.server.domain.dummy.domain.PaymentFrequency;
import lombok.Builder;

@Builder
public record ContractUpdateDto(
        String contractPeriod,
        PaymentFrequency contractPaymentFrequency,
        String contractPayer,
        String contractPaymentMethod,
        String contractConsultation

) {}
