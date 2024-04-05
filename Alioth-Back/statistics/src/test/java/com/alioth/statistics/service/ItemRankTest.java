package com.alioth.statistics.service;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.InsuranceProduct;
import com.alioth.statistics.domain.dummy.domain.InsuranceProductCategory;
import com.alioth.statistics.domain.dummy.repository.InsuranceProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class ItemRankTest {

    @Autowired private InsuranceProductRepository insuranceProductRepository;
    @Autowired private ContractRepository contractRepository;

    @Test
    @DisplayName("보험 상품별 개수")
    public void 보험상품별개수() {

        //int allSize = insuranceProductRepository.findAll().size();
        List<InsuranceProductCategory> categoryList = Arrays.stream(InsuranceProductCategory.values()).toList();
        Map<String, Integer> result = new LinkedHashMap<>();

        for (var category : categoryList) {
            List<InsuranceProduct> byInsuranceCategory = insuranceProductRepository.findByInsuranceCategory(category.getInsurance());
            //System.out.println(category.toString() + " = " + byInsuranceCategory.size());
            result.put(category.toString(), byInsuranceCategory.size());
        }

        System.out.println("result = " + result);
    }


    @Test
    @DisplayName("보험 상품계약 건 순위")
    public void 보험상품계약건순위() {

        List<InsuranceProduct> allList = insuranceProductRepository.findAll();
        int contractSize = contractRepository.findAll().size();
        //int insuranceProductSize = allList.size();
        Map<String, String> result = new HashMap<>();

        for (var InsuranceProduct: allList) {
            List<Contract> insuranceProductList = contractRepository.findByInsuranceProduct(InsuranceProduct);
            if(insuranceProductList.size() == 0) {
                continue;
            }

            double v = ((double)insuranceProductList.size() / (double)contractSize) * 100;;
            String temp = String.format("%.3f", v);
            result.put(InsuranceProduct.getInsuranceCategory(), temp + "%");
        }

        System.out.println("insuranceProductList = " + result);

    }




}
