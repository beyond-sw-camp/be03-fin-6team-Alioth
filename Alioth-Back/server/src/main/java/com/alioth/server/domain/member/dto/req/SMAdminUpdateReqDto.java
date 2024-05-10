package com.alioth.server.domain.member.dto.req;


import com.alioth.server.domain.member.domain.SalesMemberType;
import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record SMAdminUpdateReqDto(
    @Valid
    SalesMemberType rank,
    @Valid
    String teamCode,
    String performanceReview,
    Long monthlyTargetCount,
    Long monthlyTargetPrice
) {

}
