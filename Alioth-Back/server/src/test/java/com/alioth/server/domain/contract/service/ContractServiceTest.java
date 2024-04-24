package com.alioth.server.domain.contract.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.contract.domain.Contract;
import com.alioth.server.domain.contract.dto.req.ContractCreateDto;
import com.alioth.server.domain.contract.dto.req.ContractUpdateDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.contract.repository.ContractRepository;
import com.alioth.server.domain.dummy.domain.ContractStatus;
import com.alioth.server.domain.dummy.domain.PaymentFrequency;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
@WithMockUser(username = "202432")
public class ContractServiceTest {
    @Autowired
    private ContractService contractService;
    @Autowired
    private TypeChange typeChange;

    @Autowired
    private ContractRepository contractRepository;

    private Contract contract;

    @BeforeEach
    void setUp() {
        ContractCreateDto contractCreateDto = ContractCreateDto.builder()
                .contractDate(LocalDateTime.now())
                .contractExpireDate(LocalDateTime.now())
                .contractPeriod("1Y")
                .contractTotalPrice("10000")
                .contractPaymentAmount("10000")
                .contractPaymentFrequency(PaymentFrequency.Monthly)
                .contractPaymentMaturityInstallment(1L)
                .contractCount(1L)
                .contractPaymentMethod("한화카드")
                .contractPayer("테스터훈")
                .contractConsultation("테스트 보험에 매우 가입을 하고 싶어 하십니다.")
                .contractStatus(ContractStatus.New)
                .insuranceProductId(1L)
                .customId(1L)
                .contractMemberId(1L)
                .build();

        UserDetails mockUserDetails = User.builder()
                .username("202432") // memberCode 값을 username으로 사용
                .password("") // 비밀번호는 테스트에 필요하지 않으므로 빈 문자열이나 임의의 값을 사용
                .authorities("ROLE_USER") // 실제 사용자의 권한에 따라 적절한 권한을 설정
                .build();


        ContractResDto savedContractResDto = contractService.createContract(contractCreateDto, mockUserDetails);
        this.contract = contractRepository.findById(savedContractResDto.contractId())
                .orElseThrow(() -> new EntityNotFoundException("계약이 생성되지 않았습니다"));
    }

    @Test
    @DisplayName("계약 추가 테스트")
    void save(){
        assertNotNull(contract.getContractId());
    }
    @Test
    @DisplayName("계약 수정 테스트")
    void updateContract() {
        Long contractId = contract.getContractId();
        ContractUpdateDto contractUpdateDto = ContractUpdateDto.builder()
                .contractPeriod("2Y")
                .contractPaymentFrequency(PaymentFrequency.Monthly)
                .contractPayer("테스터훈2")
                .contractPaymentMethod("계좌이체")
                .contractConsultation("Updated Consultation")
                .build();

        ContractResDto updatedContract = contractService.updateContract(contractId, contractUpdateDto);

        assertEquals("Updated Consultation", updatedContract.contractConsultation());
    }

    @Test
    @DisplayName("계약 리스트 조회 테스트")
    void listAllContracts() {

        List<ContractResDto> contracts = contractService.listAllContracts();

        assertNotNull(contracts);
        assertFalse(contracts.isEmpty());

    }
    @Test
    @DisplayName("계약 삭제 테스트")
    void deleteContract() {
        Long contractId = contract.getContractId();

        contractService.deleteContract(contractId);

        assertThrows(EntityNotFoundException.class, () -> {
            contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("계약이 존재하지 않습니다."));
        });
    }
}
