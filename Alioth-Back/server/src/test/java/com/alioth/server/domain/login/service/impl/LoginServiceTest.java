package com.alioth.server.domain.login.service.impl;

import com.alioth.server.common.redis.RedisService;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.login.service.LoginService;
import com.alioth.server.domain.login.service.LogoutService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginServiceImpl;

    @Autowired
    private LogoutService logoutServiceImpl;

    @Autowired
    private RedisService redisService;

    @Test
    @DisplayName("로그인후 토큰 발급 테스트")
    public void 로그인토큰발급테스트() {
        LoginReqDto reqDto = LoginReqDto.builder()
                .memberCode(2024311L)
                .password("a1234567")
                .build();

        LoginResDto loginResDto = loginServiceImpl.memberLogin(reqDto);
        System.out.println("LoginResDto = " + loginResDto);
    }

    @Test
    @DisplayName("로그아웃 후 토큰 삭제 테스트")
    public void 로그아웃후토큰삭제테스트() {
        logoutServiceImpl.logout(2024311L);
        String values = redisService.getValues(2024311 + ":RefreshToken");
        Assertions.assertThat(values).isEqualTo("false");
    }

}