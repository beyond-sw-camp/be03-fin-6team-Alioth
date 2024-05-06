package com.alioth.server.domain.contract.controller;

import com.alioth.server.common.jwt.JwtTokenProvider;
import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.contract.dto.req.ContractCancellationDto;
import com.alioth.server.domain.contract.dto.req.ContractCreateDto;
import com.alioth.server.domain.contract.dto.req.ContractUpdateDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.service.ContractService;
import com.alioth.server.domain.dummy.domain.ContractStatus;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/server/api/contract")
public class ContractController {

    private final ContractService contractService;
    private final JwtTokenProvider  jwtTokenProvider;



    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createContract( @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid ContractCreateDto contractCreateDto) {
        ContractResDto createdContract = contractService.createContract(contractCreateDto, userDetails);
        return CommonResponse.responseMessage(HttpStatus.CREATED, "계약이 성공적으로 생성되었습니다!", createdContract);
    }
    @PatchMapping("/update/{contractId}")
    public ResponseEntity<CommonResponse> updateContract(@PathVariable("contractId") Long contractId, @RequestBody @Valid ContractUpdateDto contractUpdateDto) {
        ContractResDto updatedContract = contractService.updateContract(contractId, contractUpdateDto);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "계약이 성공적으로 변경되었습니다.",
                updatedContract
        );
    }
    @PostMapping("/cancel/{contractId}")
    public ResponseEntity<CommonResponse> cancelContract(@PathVariable("contractId") Long contractId, @RequestBody @Valid ContractCancellationDto cancellationDto) {
        contractService.cancelContract(contractId, cancellationDto.reason());
        return CommonResponse.responseMessage(HttpStatus.OK, "계약이 성공적으로 해지되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResponse> listContractsByStatus(@RequestParam(required = false) ContractStatus status) {
        List<ContractResDto> contracts = contractService.findAllContractsByStatus(status);
        return CommonResponse.responseMessage(HttpStatus.OK, "계약 목록을 성공적으로 조회했습니다.", contracts);
    }


    @GetMapping("/detail/{contractId}")
    public ResponseEntity<CommonResponse> getContractDetail(@PathVariable Long contractId) {
        ContractResDto contractDetail = contractService.getContractDetails(contractId);
        return CommonResponse.responseMessage(HttpStatus.OK, "계약 상세 정보를 성공적으로 조회했습니다.", contractDetail);
    }
}
