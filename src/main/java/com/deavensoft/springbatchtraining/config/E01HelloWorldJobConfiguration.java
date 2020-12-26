package com.deavensoft.springbatchtraining.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@Slf4j
public class E01HelloWorldJobConfiguration {

  @Bean
  Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
    return jobBuilderFactory.get("helloWorldJob")
        .start(stepBuilderFactory.get("jobStep1")
            .tasklet(
                (contribution, chunkContext) -> {
                  log.info("Hello World! Job has run!");
                  return RepeatStatus.FINISHED;
            })
            .build())
        .build();

  }
}
