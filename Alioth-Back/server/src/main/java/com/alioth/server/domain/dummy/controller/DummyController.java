package com.alioth.server.domain.dummy.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.dummy.dto.res.SimpleListDTO;
import com.alioth.server.domain.dummy.service.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/insurance-products")
    public ResponseEntity<CommonResponse> getInsuranceProducts() {
        List<SimpleListDTO> products = dummyService.getAllInsuranceProducts();
        return CommonResponse.responseMessage(HttpStatus.OK, "보험상품 목록 조회 성공", products);
    }

    @GetMapping("/contract-members")
    public ResponseEntity<CommonResponse> getContractMembers() {
        List<SimpleListDTO> members = dummyService.getAllContractMembers();
        return CommonResponse.responseMessage(HttpStatus.OK, "계약사원 목록 조회 성공", members);
    }

    @GetMapping("/customers")
    public ResponseEntity<CommonResponse> getCustomers() {
        List<SimpleListDTO> customers = dummyService.getAllCustomers();
        return CommonResponse.responseMessage(HttpStatus.OK, "고객 목록 조회 성공", customers);
    }
}
