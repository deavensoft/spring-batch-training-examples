package com.deavensoft.springbatchtraining.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@Slf4j
public class E03TaskletJobConfiguration {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  Job job() {
    return jobBuilderFactory.get("jobWithTasklet")
        .incrementer(new RunIdIncrementer())
        .start(tasklet())
//        .next(anotherTasklet())
        .build();

  }

  @Bean
  TaskletStep tasklet() {
    return stepBuilderFactory.get("taskletStep")
        .tasklet(
            new Tasklet() {
              @Override
              public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                log.info("Hello from Tasklet!");
                return RepeatStatus.FINISHED;
              }
            })
        .build();
  }

  @Bean
  TaskletStep anotherTasklet() {
    return stepBuilderFactory.get("otherTasklet")
        .tasklet(
            (contribution, chunkContext) -> {
              log.info("Another Tasklet: I'm specified using lambda expression!");
              return RepeatStatus.FINISHED;
            })
        .build();
  }
}
