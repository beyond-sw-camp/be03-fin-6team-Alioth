package com.alioth.server.domain.member.dto.res;


import com.alioth.server.domain.member.domain.SalesMemberType;
import lombok.Builder;

@Builder
public record SMTeamListResDto(
    String phone,
    String name,
    String email,
    String zoneCode,
    String roadAddress,
    String detailAddress,
    String officeAddress,
    String extensionNumber,
    String profileImage,
    Long salesMemberCode,
    SalesMemberType rank

) {

}
