package com.alioth.statistics.common.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;
import java.util.Set;

@Slf4j
@Configuration
public class JobConfiguration {

    @Bean
    @JobScope
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("[JobExecutionListener BeforeJob] jobExecution is " + jobExecution.getStatus());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    log.error("[JobExecutionListener AfterJob] jobExecution is " + jobExecution.getStatus());
                }
                log.info("[JobExecutionListener AfterJob] jobExecution is " + jobExecution.getStatus());
            }
        };
    }


    @Bean(name = "batchJob")
    public Job batchJob(JobRepository jobRepository, Map<String, Step> stepMap) {

        return new JobBuilder("batchJob", jobRepository)
                .start(stepMap.get("stepMemberSales"))
                .next(stepMap.get("stepTeamSales"))
                .next(stepMap.get("stepHqSales"))
                .next(stepMap.get("stepRankProduct"))
                .next(stepMap.get("stepRankMember"))
                .listener(jobExecutionListener())
                .build();
    }
}
