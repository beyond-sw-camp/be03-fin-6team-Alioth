package com.alioth.statistics.common.batch.tasklet;

import com.alioth.statistics.domain.batch.BatchRankProduct;
import com.alioth.statistics.domain.batch.repository.BatchRankProductRepository;
import com.alioth.statistics.domain.contract.domain.Contract;
import com.alioth.statistics.domain.contract.repository.ContractRepository;
import com.alioth.statistics.domain.dummy.domain.InsuranceProduct;
import com.alioth.statistics.domain.dummy.domain.InsuranceProductCategory;
import com.alioth.statistics.domain.dummy.repository.InsuranceProductRepository;
import com.alioth.statistics.domain.member.repository.SalesMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RankProductTasklet {

    private final ContractRepository contractRepository;
    private final InsuranceProductRepository insuranceProductRepository;
    private final BatchRankProductRepository batchRankProductRepository;

    @Bean(name = "taskletRankProduct")
    public Tasklet taskletRankProduct(){
        return ((contribution, chunkContext) -> {
            log.info("================================================");
            log.info("===========This is taskletRankProduct===========");
            // 년, 월, 시, 일, 분
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            int month = now.getMonth().getValue();
            int day = now.getDayOfMonth();
            int hour = now.getHour();
            int minute = now.getMinute();
            LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute, 0, 0);

            List<BatchRankProduct> productList = new LinkedList<>();
            List<InsuranceProduct> insuranceProductList = insuranceProductRepository.findAll();

            for (var product : insuranceProductList) {
                BigDecimal productPrice = contractRepository.findByInsuranceProduct(product).stream()
                        .map(contract -> new BigDecimal(contract.getContractTotalPrice()))
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);

                long productCount = contractRepository.findByInsuranceProduct(product).stream().count();

                BatchRankProduct rankProduct = BatchRankProduct.builder()
                        .productName(product.getInsuranceName())
                        .productCode(product.getInsuranceMainCode())
                        .productCategory(product.getInsuranceCategory())
                        .contractPrice(productPrice.toPlainString())
                        .contractCount(String.valueOf(productCount))
                        .createdDate(time)
                        .build();

                productList.add(rankProduct);
            }

            productList.sort((o1, o2) -> {
                BigDecimal o2Price = new BigDecimal(o2.getContractPrice());
                BigDecimal o1Price = new BigDecimal(o1.getContractPrice());

                return o2Price.subtract(o1Price).compareTo(BigDecimal.ZERO);
            });

            batchRankProductRepository.saveAll(productList);

            log.info("===========This is taskletRankProduct===========");
            log.info("================================================");

            return RepeatStatus.FINISHED;
        });
    }
}
