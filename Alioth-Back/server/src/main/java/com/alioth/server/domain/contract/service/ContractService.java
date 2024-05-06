package com.alioth.server.domain.contract.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.contract.domain.Contract;
import com.alioth.server.domain.contract.dto.req.ContractCancellationDto;
import com.alioth.server.domain.contract.dto.req.ContractCreateDto;
import com.alioth.server.domain.contract.dto.req.ContractUpdateDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.repository.ContractRepository;
import com.alioth.server.domain.dummy.domain.ContractMembers;
import com.alioth.server.domain.dummy.domain.ContractStatus;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.dummy.domain.InsuranceProduct;
import com.alioth.server.domain.dummy.service.DummyService;
import com.alioth.server.domain.excel.dto.ExcelReqDto;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final DummyService dummyService;
    private final TypeChange typeChange;
    private final SalesMemberService salesMemberService;


    public ContractResDto createContract(ContractCreateDto dto, UserDetails userDetails) {
        // 사용자 인증정보를 기반으로 SalesMembers 객체를 조회
        SalesMembers salesMember = getSalesMemberFromUsername(userDetails.getUsername());

        // 각 ID 값을 기반으로 도메인 객체를 조회
        ContractMembers contractMembers = dummyService.contractManagerFindById(dto.contractMemberId());
        Custom custom = dummyService.customFindById(dto.customId());
        InsuranceProduct insuranceProduct = dummyService.insuranceProductFindById(dto.insuranceProductId());
        String contractCode = this.createContractCode();

        // Contract 객체 생성 및 저장
        Contract contract = typeChange.ContractCreateDtoToContract(contractCode, dto, contractMembers, custom, insuranceProduct, salesMember);
        contract = contractRepository.save(contract);

        // 결과 변환 및 반환
        return typeChange.ContractToContractResDto(contract);
    }

    private SalesMembers getSalesMemberFromUsername(String username) {
        try {
            Long salesMemberCode = Long.parseLong(username);
            return salesMemberService.findBySalesMemberCode(salesMemberCode);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("사용자 이름이 유효한 SalesMember 코드가 아닙니다: " + username);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("SalesMember를 찾을 수 없습니다: " + username);
        }
    }


    @Transactional
    public ContractResDto updateContract(Long contractId, ContractUpdateDto dto) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("계약을 찾을 수 없습니다."));
        contract.update(dto);
        contract = contractRepository.save(contract);
        return typeChange.ContractToContractResDto(contract);
    }


    public void deleteContract(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("계약을 찾을 수 없습니다."));
        contract.cancel();  // 계약 상태를 Cancellation으로 변경
        contractRepository.save(contract);  // 변경된 상태를 저장
    }

    public List<ContractResDto> findAllContractsByStatus(ContractStatus status) {
        List<Contract> contracts;
        if (status == null) {
            contracts = contractRepository.findAll();
        } else {
            contracts = contractRepository.findAllByContractStatus(status);
        }
        return contracts.stream()
                .map(typeChange::ContractToContractResDto)
                .toList();
    }


    public List<ContractResDto> listAllContracts() {
        return contractRepository.findAll().stream()
                    .map(typeChange::ContractToContractResDto)
                    .toList();
    }

    public List<ContractResDto> findAllContractsByPeriod(ExcelReqDto dto) {
        if(dto.startDate() == null && dto.endDate() == null){
            return this.listAllContracts();
        } else {
            return contractRepository.findAllByPeriod(dto.startDate(), dto.endDate()).stream()
                    .map(typeChange::ContractToContractResDto).toList();
        }
    }

    public List<ContractResDto> allContractsByMemberAndPeriod(Long memberId, ExcelReqDto dto){
        List<ContractResDto> contractResDtoList = new ArrayList<>();
        List<Contract> allContracts;
        if(dto.startDate() == null && dto.endDate() == null){
            allContracts = contractRepository.findAllBySalesMembersId(memberId);
            for (Contract c: allContracts){
                contractResDtoList.add(typeChange.ContractToContractResDto(c));
            }
        } else {
            allContracts = contractRepository.findAllByPeriodAndSalesMembersId(memberId, dto.startDate(), dto.endDate());
            for (Contract c: allContracts){
                contractResDtoList.add(typeChange.ContractToContractResDto(c));
            }
        }
        return contractResDtoList;
    }


    public List<Custom> customListByMemberId(Long memberId, ExcelReqDto dto) {
        if(dto.startDate() == null && dto.endDate() == null){
          return contractRepository.findAllBySalesMembersId(memberId).stream().map(Contract::getCustom).toList();
        } else {
            return contractRepository.findAllByPeriodAndSalesMembersId(memberId,dto.startDate(),dto.endDate())
                    .stream().map(Contract::getCustom).toList();
        }
    }

    public List<Custom> customTotalList(ExcelReqDto dto) {
        if(dto.startDate() == null && dto.endDate() == null){
            return contractRepository.findAll().stream().map(Contract::getCustom).toList();
        } else {
            return contractRepository.findAllByPeriod(dto.startDate(),dto.endDate()).stream()
                    .map(Contract::getCustom).toList();
        }
    }
    public String createContractCode(){
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return date + UUID.randomUUID();
    }

    public List<Custom> customListByMemberId(Long memberId) {
        SalesMembers sm = salesMemberService.findById(memberId);
        return contractRepository.findAllBySalesMembersId(sm.getId()).stream().map(Contract::getCustom).toList();
    }

    public List<Custom> customTotalList() {
        return contractRepository.findAll().stream().map(Contract::getCustom).toList();
    }

    public ContractResDto getContractDetails(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("계약을 찾을 수 없습니다: " + contractId));
        return typeChange.ContractToContractResDto(contract);
    }
    public void cancelContract(Long contractId, String reason) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("계약을 찾을 수 없습니다: " + contractId));
        contract.cancel(reason);
        contractRepository.save(contract);
    }


    public List<ContractResDto> findByNoTeamList(ExcelReqDto dto) {

        if(dto.startDate() == null && dto.endDate() == null ){
            return contractRepository.findByAllNoTeamList().stream().map(typeChange::ContractToContractResDto).toList();
        }else{
            return contractRepository.findByAllNoTeamNotDateList(dto.startDate(),dto.endDate()).stream().map(typeChange::ContractToContractResDto).toList();
        }

    }
}

