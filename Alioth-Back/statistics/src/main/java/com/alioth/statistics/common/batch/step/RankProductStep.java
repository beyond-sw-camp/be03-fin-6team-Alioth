package com.alioth.statistics.common.batch.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class RankProductStep {

    @Bean(name = "stepRankProduct")
    public Step stepRankProduct(JobRepository jobRepository, @Qualifier("taskletRankProduct") Tasklet taskletRankProduct, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("stepRankProduct", jobRepository)
                // .allowStartIfComplete(true)     // test 를 위해 Step이 항상 재실행되도록 설정
                .tasklet(taskletRankProduct, platformTransactionManager)
                .build();
    }
}
