package com.alioth.server.domain.member.dto.req;


import com.alioth.server.domain.member.domain.SalesMemberType;
import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record SalesMemberAdminUpdateReqDto(
    @Valid
    SalesMemberType rank,
    @Valid
    String teamCode
) {

}
