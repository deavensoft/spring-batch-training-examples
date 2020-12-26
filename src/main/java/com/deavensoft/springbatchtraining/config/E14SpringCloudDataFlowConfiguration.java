package com.deavensoft.springbatchtraining.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@Slf4j
public class E14SpringCloudDataFlowConfiguration {

  @Bean
  Job job(JobBuilderFactory jobBuilderFactory,
          StepBuilderFactory stepBuilderFactory) {

    return jobBuilderFactory.get("helloEnjoying")
        .start(stepBuilderFactory.get("jobStep1")
            .tasklet(
                (contribution, chunkContext) -> {
                  String name = chunkContext.getStepContext().getStepExecution().getJobParameters()
                      .getString("name");
                  log.info("Hello {}! Job has enjoyed running!", name != null ?  name : "World");
                  return RepeatStatus.FINISHED;
            })
            .build())
        .incrementer(new RunIdIncrementer())
        .build();
  }
}
