package com.alioth.statistics.service.insuranceproduct.impl;

import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.InsuranceProduct;
import com.alioth.statistics.domain.dummy.domain.InsuranceProductCategory;
import com.alioth.statistics.domain.dummy.repository.InsuranceProductRepository;
import com.alioth.statistics.service.insuranceproduct.InsuranceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceProductRankService implements InsuranceProductService {

    private final InsuranceProductRepository insuranceProductRepository;
    private final ContractRepository contractRepository;

    @Override
    public Map<String, String> insuranceContractRankService() {
        List<InsuranceProduct> allList = insuranceProductRepository.findAll();
        int contractSize = contractRepository.findAll().size();
        Map<String, String> temp = new HashMap<>();
        Map<String, String> result = new LinkedHashMap<>();

        for (var InsuranceProduct: allList) {
            List<Contract> insuranceProductList = contractRepository.findByInsuranceProduct(InsuranceProduct);
            if(insuranceProductList.size() == 0) {
                continue;
            }

            double v = ((double)insuranceProductList.size() / (double)contractSize) * 100;;
            String strTemp = String.format("%.3f", v);
            temp.put(InsuranceProduct.getInsuranceCategory(), strTemp + "%");
        }

        List<String> keySet = new ArrayList<>(temp.keySet());
        keySet.sort((o1, o2) -> temp.get(o2).compareTo(temp.get(o1)));
        for (String key : keySet) {
            System.out.print("Key : " + key);
            System.out.println(", Val : " + temp.get(key));
            result.put(key, temp.get(key));
        }

        return result;
    }

    @Override
    public Map<String, Integer> insuranceProductRankService() {
        List<InsuranceProductCategory> categoryList = Arrays.stream(InsuranceProductCategory.values()).toList();
        Map<String, Integer> temp = new HashMap<>();
        Map<String, Integer> result = new LinkedHashMap<>();

        for (var category : categoryList) {
            List<InsuranceProduct> byInsuranceCategory = insuranceProductRepository.findByInsuranceCategory(category.getInsurance());
            //System.out.println(category.toString() + " = " + byInsuranceCategory.size());
            temp.put(category.getInsurance(), byInsuranceCategory.size());
        }

        List<String> keySet = new ArrayList<>(temp.keySet());
        keySet.sort((o1, o2) -> temp.get(o2).compareTo(temp.get(o1)));
        for (String key : keySet) {
//            System.out.print("Key : " + key);
//            System.out.println(", Val : " + temp.get(key));
            result.put(key, temp.get(key));
        }

        return result;
    }
}