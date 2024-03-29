package com.alioth.server.domain.contract.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.contract.domain.Contract;
import com.alioth.server.domain.contract.dto.req.ContractCreateDto;
import com.alioth.server.domain.contract.dto.req.ContractUpdateDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.repository.ContractRepository;
import com.alioth.server.domain.dummy.domain.ContractMembers;
import com.alioth.server.domain.dummy.domain.Custom;
import com.alioth.server.domain.dummy.domain.InsuranceProduct;
import com.alioth.server.domain.dummy.service.DummyService;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.service.SalesMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

        // Contract 객체 생성 및 저장
        Contract contract = typeChange.ContractCreateDtoToContract(dto, contractMembers, custom, insuranceProduct, salesMember);
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
        if (!contractRepository.existsById(contractId)) {
            throw new EntityNotFoundException("계약을 찾을 수 없습니다.");
        }
        contractRepository.deleteById(contractId);
    }

    public List<ContractResDto> listAllContracts() {
        return contractRepository.findAll().stream()
                .map(contract -> typeChange.ContractToContractResDto(contract))
                .collect(Collectors.toList());
    }
}
