package com.alioth.server.domain.member.dto.req;

import com.alioth.server.domain.member.domain.SalesMemberType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SalesMemberUpdateReqDto(
        String email,
        String phone,
        String birthDay,
        String officeAddress,
        String extensionNumber,
        String zoneCode,
        String roadAddress,
        String detailAddress

){}
