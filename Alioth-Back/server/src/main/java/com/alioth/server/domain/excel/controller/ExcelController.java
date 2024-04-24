package com.alioth.server.domain.excel.controller;

import com.alioth.server.domain.excel.dto.ExcelReqDto;
import com.alioth.server.domain.excel.service.ExcelService;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final SalesMemberService salesMemberService;
    private final ExcelService excelService;


    @PostMapping(value = {"/export/{type}", "/export/{type}/{code}"})
    public void downloadContractInfo(HttpServletResponse response,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody ExcelReqDto dto,
                                     @PathVariable(required = false) String type,
                                     @PathVariable(required = false) String code
    ) throws IOException, IllegalAccessException {
        SalesMembers salesMember = salesMemberService.findBySalesMemberCode(Long.parseLong(userDetails.getUsername()));
        switch (type){
            case "contract":
                excelService.contractExcel(salesMember, code, response, dto);
                break;
            case "customerList":
                excelService.customerListExcel(salesMember, code, response, dto);
                break;
            case "salesMembers":
                excelService.salesMembersExcel(salesMember, code, response);
                break;
        }
    }
}