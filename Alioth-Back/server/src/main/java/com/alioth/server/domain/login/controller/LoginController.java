package com.alioth.server.domain.login.controller;

import com.alioth.server.common.aws.SMSService;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.login.service.LoginService;
import com.alioth.server.domain.login.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LogoutService logoutService;
    private final SMSService smsService;
    private final StringRedisTemplate stringRedisTemplate;

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

    @PostMapping("/api/send-verification")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> payload) {
        String phoneNumber = payload.get("phone");
        String verificationCode = loginService.generateVerificationCode(6);

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(phoneNumber, verificationCode, 5, TimeUnit.MINUTES); //

        smsService.sendSMS(phoneNumber, "[alioth] 본인확인 인증번호는 " + verificationCode + "입니다");

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "인증번호가 발송되었습니다."
        );
    }


    @PostMapping("/api/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload) {
        String phoneNumber = payload.get("phone");
        String code = payload.get("code");
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String storedCode = ops.get(phoneNumber);

        boolean isVerified = storedCode != null && storedCode.equals(code);
        if(isVerified) {
            stringRedisTemplate.delete(phoneNumber);
        }

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                isVerified ? "인증에 성공하였습니다." : "인증번호가 일치하지 않습니다."
        );
    }
}
