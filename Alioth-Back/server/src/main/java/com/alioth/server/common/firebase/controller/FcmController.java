package com.alioth.server.common.firebase.controller;

import com.alioth.server.common.firebase.domain.FcmSendDto;
import com.alioth.server.common.firebase.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alioth.server.common.response.CommonResponse;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FcmController {

    private final FcmService fcmService;
    @PostMapping("/send")
    public ResponseEntity<CommonResponse> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto) throws IOException {
        log.debug("메시지를 전송합니다.");
        int result = fcmService.sendMessageTo(fcmSendDto);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "메시지가 성공적으로 전송되었습니다.",
                result
        );
    }
}