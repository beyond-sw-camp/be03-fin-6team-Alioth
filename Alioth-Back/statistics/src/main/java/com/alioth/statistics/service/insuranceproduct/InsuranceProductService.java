package com.alioth.statistics.service.insuranceproduct;

import java.util.Map;

public interface InsuranceProductService {
    Map<String, String> insuranceContractRankService();
    Map<String, Integer> insuranceProductRankService();
}
