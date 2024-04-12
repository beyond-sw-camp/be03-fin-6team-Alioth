package com.alioth.server.domain.member.controller;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.*;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class SalesMemberController {

    private final SalesMemberService salesMemberService;

    @PostMapping("/create")
    public ResponseEntity<?> createMember(@RequestBody @Valid SalesMemberCreateReqDto dto) {
        SalesMembers member = salesMemberService.create(dto);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message(member.getName() + "님의 회원가입이 완료되었습니다.")
                .result(member.getSalesMemberCode())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commonResponse);
    }

    // Change the path variable from {id} to {salesMemberCode}
    @PatchMapping("/{salesMemberCode}/password")
    public ResponseEntity<?> updateMemberPassword(@RequestBody @Valid SalesMemberUpdatePassword dto,
                                                  @PathVariable("salesMemberCode") Long salesMemberCode) {
        SalesMembers member = salesMemberService.updatePassword(dto, salesMemberCode);

        CommonResponse commonResponse = CommonResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(member.getName() + "님의 비밀번호가 변경되었습니다.")
                .result(member.getSalesMemberCode())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(commonResponse);
    }


    @PatchMapping("/{id}/info")
    public ResponseEntity<?> updateMemberInfo(@PathVariable("id")Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }


    //HJ
    //관리자 사원 부서, 권한, 고과평가 수정
    @PatchMapping("/admin/update/{memberId}")
    public ResponseEntity<CommonResponse> updateMemberAdminInfo(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Valid SMAdminUpdateReqDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if(salesMemberService.findBySalesMemberCode(
                Long.parseLong(userDetails.getUsername())).getRank() != SalesMemberType.FP){
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "successfully updated",
                    salesMemberService.adminMemberUpdate(memberId,dto)
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

//    사원 상세 조회 (지점장, 지역장)
    @GetMapping("/details/{memberId}")
    public ResponseEntity<CommonResponse> getMemberInfoAdmin(
            @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (salesMemberService.findBySalesMemberCode(
                Long.parseLong(userDetails.getUsername())).getRank() != SalesMemberType.FP) {
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "success",
                    salesMemberService.memberDetail(memberId)
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    //사원 정보 수정 (사원 본인만)
    @PatchMapping("/details/update")
    public ResponseEntity<CommonResponse> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid SalesMemberUpdateReqDto dto) {
        Long memberId = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername())).getId();
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "success",
                salesMemberService.updateMyInfo(memberId, dto)
        );
    }
}
