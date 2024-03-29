package com.alioth.server.domain.dummy.service;

import com.alioth.server.domain.contract.repository.RenewalRepository;
import com.alioth.server.domain.dummy.domain.*;
import com.alioth.server.domain.dummy.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DummyService {

    private final RenewalRepository renewalRepository;
    private final ContractMembersRepository contractMembersRepository;
    private final InsuranceProductRepository insuranceProductRepository;
    private final CustomRepository customRepository;

    @Transactional
    public void addData(
            long CMSize,
            long ProductSize,
            long CustomSize
    ) {

        Faker faker = new Faker();
        List<ContractMembers> contractMembersList = new ArrayList<>();
        List<Custom> customList = new ArrayList<>();
        List<InsuranceProduct> insuranceProductList = new ArrayList<>();

        for (int i = 0; i < CMSize; i++) {
            // 계약 사원 더미 데이터
            String ContractMembersCode = faker.code().asin();
            String ContractMembersName = faker.name().fullName();
            ContractMembers contractMember = ContractMembers.builder()
                    .CM_code(ContractMembersCode)
                    .CM_name(ContractMembersName)
                    .build();
            contractMembersList.add(contractMember);
        }

        for (int i = 0; i < ProductSize; i++) {
            // 보험상품 더미 데이터
            String insuranceProductName = faker.commerce().productName();
            String insuranceProductCategory = faker.options().option("생명보험", "건강보험", "종신보험", "자동차보험", "여행자보험");
            String insuranceProductSubCode = faker.code().ean8();
            String insuranceProductMainCode = faker.code().ean13();

            InsuranceProduct insuranceProduct = InsuranceProduct.builder()
                    .insuranceName(insuranceProductName)
                    .insuranceCategory(insuranceProductCategory)
                    .insuranceSubCode(insuranceProductSubCode)
                    .insuranceMainCode(insuranceProductMainCode)
                    .build();

            insuranceProductList.add(insuranceProduct);
        }

        for (int i = 0; i < CustomSize; i++) {

            // 고객 더미 데이터
            String CustomCode = faker.code().asin();
            String CustomName = faker.name().fullName();
            String phoneNumber = faker.phoneNumber().cellPhone();
            String address = faker.address().fullAddress();
            String grade = faker.options().option("Gold", "Silver", "Bronze");

            Custom custom = Custom.builder()
                    .customerCode(CustomCode)
                    .customerName(CustomName)
                    .customerPhoneNumber(phoneNumber)
                    .customerAddress(address)
                    .customerGrade(grade)
                    .build();

            customList.add(custom);
        }
        contractMembersRepository.saveAll(contractMembersList);
        customRepository.saveAll(customList);
        insuranceProductRepository.saveAll(insuranceProductList);
    }

    public ContractMembers contractManagerFindById(Long cm_id) {
        return contractMembersRepository.findById(cm_id).orElseThrow(() -> new EntityNotFoundException("계약사원이 존재하지않습니다."));
    }

    public Custom customFindById(Long customer_id) {
        return customRepository.findById(customer_id).orElseThrow(() -> new EntityNotFoundException("고객이 존재하지않습니다."));
    }

    public InsuranceProduct insuranceProductFindById(Long insurance_id) {
        return insuranceProductRepository.findById(insurance_id).orElseThrow(() -> new EntityNotFoundException("보험상품이 존재하지않습니다."));
    }
}
