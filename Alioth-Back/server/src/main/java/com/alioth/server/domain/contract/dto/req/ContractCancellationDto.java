package com.alioth.server.domain.contract.dto.req;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;

@Builder
public record ContractCancellationDto(
        @NotBlank(message = "해약 사유는 필수입니다.")
        String reason
) {}
