package com.alioth.server.domain.member.dto.res;


import com.alioth.server.domain.member.domain.SalesMemberType;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Builder
public record SalesMemberResDto(
        String email,
        Long salesMemberCode,
        String phone,
        String name,
        String birthDay,
        String zoneCode,
        String roadAddress,
        String detailAddress,
        String profileImage,
        String performanceReview,
        String teamCode,
        String teamName,
        String officeAddress,
        String extensionNumber,
        SalesMemberType rank

) {


}
