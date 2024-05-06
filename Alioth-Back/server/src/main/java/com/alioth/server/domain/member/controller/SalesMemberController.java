package com.alioth.server.domain.member.controller;

import com.alioth.server.common.aws.S3Service;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/server/api/members")
public class SalesMemberController {

    private final SalesMemberService salesMemberService;
    private final S3Service s3Service;

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


    @PatchMapping("/{salesMemberCode}/info")
    public ResponseEntity<?> updateMemberInfo(@PathVariable("salesMemberCode") Long salesMemberCode) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }


    @PatchMapping("/{memberCode}/image")
    public ResponseEntity<?> updateMemberImage(@PathVariable("memberCode") String memberCode,
                                               @ModelAttribute SalesMemberImageReqDto memberImage) throws IOException {
        String profileImage = s3Service.saveFile(memberImage.memberImage());
        salesMemberService.updateMemberImage(memberCode, profileImage);



        return ResponseEntity.status(HttpStatus.OK)
                .body(profileImage);
    }



    //HJ
    //관리자 사원 부서, 권한, 고과평가 수정
    @PatchMapping("/admin/update/{salesMemberCode}")
    public ResponseEntity<CommonResponse> updateMemberAdminInfo(
            @PathVariable("salesMemberCode") Long salesMemberCode,
            @RequestBody @Valid SMAdminUpdateReqDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (salesMemberService.findBySalesMemberCode(
                Long.parseLong(userDetails.getUsername())).getRank() != SalesMemberType.FP
                && Long.parseLong(userDetails.getUsername())!=salesMemberCode) {
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "successfully updated",
                    salesMemberService.adminMemberUpdate(salesMemberCode, dto)
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    //    사원 상세 조회 (지점장, 지역장)
    @GetMapping("/details/{salesMemberCode}")
    public ResponseEntity<CommonResponse> getMemberInfoAdmin(
            @PathVariable("salesMemberCode") Long salesMemberCode,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (salesMemberService.findBySalesMemberCode(
                Long.parseLong(userDetails.getUsername())).getRank() != SalesMemberType.FP ||
                Long.parseLong(userDetails.getUsername()) == salesMemberService.findBySalesMemberCode(salesMemberCode).getSalesMemberCode()) {
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "success",
                    salesMemberService.memberDetail(salesMemberCode)
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



        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "success",
                salesMemberService.updateMyInfo(Long.parseLong(userDetails.getUsername()), dto)
        );
    }

    //전체 사원 목록
    @GetMapping("/list")
    public ResponseEntity<CommonResponse> getAllMemberList(
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (salesMemberService.findBySalesMemberCode(
                Long.parseLong(userDetails.getUsername())).getRank() == SalesMemberType.HQ) {
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "success",
                    salesMemberService.getAllMembers()
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    //FP 목록만 불러오기 (team 추가 멤버)
    @GetMapping("/list/FP")
    public ResponseEntity<CommonResponse> FPMemberList(@AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (salesMemberService.findBySalesMemberCode(
                Long.parseLong(userDetails.getUsername())).getRank() != SalesMemberType.FP) {
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "success",
                    salesMemberService.getAllFPMembers()
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    //Manager 목록만 불러오기 (팀장)
    @GetMapping("/list/manager")
    public ResponseEntity<CommonResponse> ManagerMemberList(@AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        if (salesMemberService.findBySalesMemberCode(
           Long.parseLong(userDetails.getUsername())).getRank() == SalesMemberType.HQ) {
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "success",
                    salesMemberService.getAllManagerMembers()
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    @DeleteMapping("/delete/{salesMemberCode}")
    public ResponseEntity<CommonResponse> deleteMember(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable("salesMemberCode") Long salesMemberCode
    ) throws AccessDeniedException {
        if (salesMemberService.findBySalesMemberCode(
                Long.parseLong(userDetails.getUsername())).getRank() == SalesMemberType.HQ) {
            salesMemberService.deleteMember(salesMemberCode);
            log.info("확인"+salesMemberService.findBySalesMemberCode(salesMemberCode).getQuit());
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "퇴사처리 되었습니다."
            );
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    @GetMapping("/validate/{employeeNumber}")
    public ResponseEntity<CommonResponse> validateEmployeeNumber(@PathVariable("employeeNumber") Long employeeNumber) {
        boolean isValid = salesMemberService.existsBySalesMemberCode(employeeNumber);
        if (isValid) {
            return CommonResponse.responseMessage(
                    HttpStatus.OK,
                    "사원번호 유효합니다.",
                    null
            );
        } else {
            return CommonResponse.responseMessage(
                    HttpStatus.NOT_FOUND,
                    "사원번호를 다시 한 번 확인해주세요.",
                    null
            );
        }
    }
}

