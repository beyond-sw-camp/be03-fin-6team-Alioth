package com.alioth.server.domain.login.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.login.service.LoginService;
import com.alioth.server.domain.login.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LogoutService logoutService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto dto) {
        LoginResDto loginResDto = loginService.memberLogin(dto);

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "로그인이 되었습니다",
                loginResDto
        );
    }

    @PostMapping("/api/{memberCode}/logout")
    public ResponseEntity<?> logout(@PathVariable String memberCode) {
        logoutService.logout(Long.valueOf(memberCode));

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "로그아웃 되었습니다"
        );
    }

    @GetMapping("/api/test")
    public String testUrl() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return name;
    }


}
