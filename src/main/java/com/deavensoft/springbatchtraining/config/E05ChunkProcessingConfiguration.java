package com.deavensoft.springbatchtraining.config;

import com.deavensoft.springbatchtraining.app.chunk.processor.Add10ItemProcessor;
import com.deavensoft.springbatchtraining.app.chunk.reader.InMemoryReader;
import com.deavensoft.springbatchtraining.app.chunk.writer.ConsoleItemWriter;
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
public class E05ChunkProcessingConfiguration {

  @Bean
  InMemoryReader itemReader(){
    return new InMemoryReader();
  }

  @Bean
  Add10ItemProcessor itemProcessor() {
    return new Add10ItemProcessor();
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
                    Tasklet helloWorldTasklet){
    return stepBuilderFactory.get("step1")
        .tasklet(helloWorldTasklet)
        .build();
  }


  @Bean
  Step step2(StepBuilderFactory stepBuilderFactory,
             InMemoryReader itemReader,
             Add10ItemProcessor itemProcessor){
    return stepBuilderFactory.get("step2").
        <Integer,Integer>chunk(3)
        .reader(itemReader)
        .processor(itemProcessor)
        .writer(new ConsoleItemWriter())
        .build();
  }

  @Bean
  Job chunkProcessingJob(JobBuilderFactory jobBuilderFactory, Step step1, Step step2){
    return jobBuilderFactory.get("chunkProcessingJob")
        .incrementer(new RunIdIncrementer())
        .start(step1)
        .next(step2)
        .build();
  }


}
