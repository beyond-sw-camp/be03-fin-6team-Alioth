package com.alioth.server.domain.member.dto.req;


import com.alioth.server.domain.member.domain.SalesMemberType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SalesMemberCreateReqDto(
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    String email,

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")
    String phone,

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    String name,
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    String password,

    @JsonFormat(pattern = "yyyyMMdd")
    String birthDay,

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    String address,

    @Valid
    SalesMemberType rank


) {

}
