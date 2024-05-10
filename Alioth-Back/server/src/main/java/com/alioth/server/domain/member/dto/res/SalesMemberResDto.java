package com.alioth.server.domain.member.dto.res;


import com.alioth.server.domain.member.domain.SalesMemberType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Builder
public record SalesMemberResDto(
        @Column(name = "이메일")
        String email,
        @Column(name = "영업 사원 코드")
        Long salesMemberCode,
        @Column(name = "연락처")
        String phone,
        @Column(name = "이름")
        String name,
        @Column(name = "생년월일")
        String birthDay,
        @Column(name = "구역 코드")
        String zoneCode,
        @Column(name = "도로명 주소")
        String roadAddress,
        @Column(name = "상세 주소")
        String detailAddress,
        @Column(name = "프로필 사진 경로")
        String profileImage,
        @Column(name = "고과 등급")
        String performanceReview,
        @Column(name = "팀 코드")
        String teamCode,
        @Column(name = "팀 이름")
        String teamName,
        @Column(name = "회사 주소")
        String officeAddress,
        @Column(name = "내선 번호")
        String extensionNumber,
        @Column(name = "목표 금액")
        Long monthlyTargetPrice,
        @Column(name = "목표 건수")
        Long monthlyTargetCount,

        @Column(name = "직급")
        SalesMemberType rank


) {


}
