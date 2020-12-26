package com.deavensoft.springbatchtraining.config;

import com.deavensoft.springbatchtraining.app.listener.HwJobExecutionListener;
import com.deavensoft.springbatchtraining.app.listener.HwStepExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@Slf4j
public class E04JobStepListenerConfiguration {

  @Bean
  HwJobExecutionListener hwJobExecutionListener() {
    return new HwJobExecutionListener();
  }

  @Bean
  HwStepExecutionListener hwStepExecutionListener() {
    return new HwStepExecutionListener();
  }

  @Bean
  Tasklet helloWorldTasklet(){
    return ((contribution, chunkContext) -> {
      log.info("Hello world  " );
      return RepeatStatus.FINISHED;
    });
  }

  @Bean
  Step step1(StepBuilderFactory stepBuilderFactory,
             HwStepExecutionListener hwStepExecutionListener,
             Tasklet helloWorldTasklet){
    return stepBuilderFactory.get("step1")
        .listener(hwStepExecutionListener)
        .tasklet(helloWorldTasklet)
        .build();
  }

  @Bean
  Job helloWorldJob(JobBuilderFactory jobBuilderFactory,
                    HwJobExecutionListener hwJobExecutionListener,
                    Step step1){
    return jobBuilderFactory.get("listenerExampleJob")
        .incrementer(new RunIdIncrementer())
        .listener(hwJobExecutionListener)
        .start(step1)
        .build();
  }

}
