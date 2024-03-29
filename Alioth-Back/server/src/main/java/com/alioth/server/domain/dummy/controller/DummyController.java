package com.alioth.server.domain.dummy.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.dummy.service.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dummy")
@RequiredArgsConstructor
public class DummyController {

    private final DummyService dummyService;

    @GetMapping("/add/CMSize/{CMSize}/ProductSize/{ProductSize}/CustomSize/{CustomSize}")
    public ResponseEntity<CommonResponse> addDummy(
            @PathVariable long CMSize,
            @PathVariable long ProductSize,
            @PathVariable long CustomSize
    ){
        dummyService.addData(CMSize,ProductSize,CustomSize);
        return CommonResponse.responseMessage(
                HttpStatus.CREATED,
                "더미데이터가 추가 되었습니다."
        );
    }
}
