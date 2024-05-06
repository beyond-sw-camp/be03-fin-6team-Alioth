package com.alioth.server.common.config;

import com.alioth.server.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {
    @GetMapping("/")
    public ResponseEntity<CommonResponse> HealthCheck(){

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "서버 이상 무"
        );
    }
    @GetMapping("/server/")
    public ResponseEntity<CommonResponse> HealthCheck2(){

        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "server 이상 무"
        );
    }
}
