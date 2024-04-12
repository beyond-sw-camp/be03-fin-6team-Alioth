package com.alioth.statistics.service.insurance;

import com.alioth.statistics.domain.insurance.dto.res.InsuranceGodResDto;

public interface InsuranceGodService {
    InsuranceGodResDto insuranceOfGodMonth();
    InsuranceGodResDto insuranceOfGodQuarter();
    InsuranceGodResDto insuranceOfGodYear();
}
