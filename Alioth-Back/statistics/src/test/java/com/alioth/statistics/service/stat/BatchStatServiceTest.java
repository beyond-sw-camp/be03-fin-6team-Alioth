package com.alioth.statistics.service.stat;

import com.alioth.statistics.domain.batch.repository.BatchMemberSalesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class BatchStatServiceTest {

    @Autowired
    private BatchMemberSalesRepository batchMemberSalesRepository;

    @Autowired
    private StatMemberSalesService batchStatService;

    @Test
    @DisplayName("일별 맴버 결과 가져오기")
    @Transactional
    public void 일별맴버결과가져오기() {

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = 0;
        int minute = 0;

        LocalDateTime endTime = LocalDateTime.of(year, month, 10, hour, minute);
        LocalDateTime startTime = endTime.minusDays(1L);

        System.out.println("startTime = " + batchMemberSalesRepository.findByCreatedTimeBetween(startTime, endTime));
    }

    @Test
    @Transactional
    public void 일별맴버결과가져오기02() {
        System.out.println("batchMemberSalesRepository = " + batchStatService.memberSalesMonth());
    }
}