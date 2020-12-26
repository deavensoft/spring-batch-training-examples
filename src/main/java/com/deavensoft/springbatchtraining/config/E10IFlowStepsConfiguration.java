package com.deavensoft.springbatchtraining.config;

import com.deavensoft.springbatchtraining.app.chunk.processor.ProductProcessor;
import com.deavensoft.springbatchtraining.app.chunk.writer.ConsoleItemWriter;
import com.deavensoft.springbatchtraining.app.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
@Slf4j
public class E10IFlowStepsConfiguration {

  private static final String CSV_INPUT_FILE_PATH = "input/product.csv";
  private static final String JSON_INPUT_FILE_PATH = "input/product.json";

  @StepScope
  @Bean
  FlatFileItemReader flatFileItemReader(){

    FlatFileItemReader<Product> reader = new FlatFileItemReader<>();

    // 1. let reader know where is the file
    ClassPathResource inputResource = new ClassPathResource(CSV_INPUT_FILE_PATH);
    reader.setResource(inputResource);

    // 2. create the line Mapper
    reader.setLineMapper(
        new DefaultLineMapper<>(){
          {
            setLineTokenizer( new DelimitedLineTokenizer() {
              {
                setNames( new String[]{"productId","name","description","price","unit"});
                setDelimiter(",");
              }
            });

            setFieldSetMapper( new BeanWrapperFieldSetMapper(){
              {
                setTargetType(Product.class);
              }
            });
          }
        }

    );

    // 3. tell reader to skip the header
    reader.setLinesToSkip(1);
    return reader;

  }

  @StepScope
  @Bean
  JsonItemReader<Product> jsonItemReader(){
    ClassPathResource inputResource = new ClassPathResource(JSON_INPUT_FILE_PATH);
    return new JsonItemReader(inputResource, new JacksonJsonObjectReader(Product.class));

  }


  @Bean
  Step csvReadStep(StepBuilderFactory stepBuilderFactory,
                          FlatFileItemReader flatFileItemReader) {
    return stepBuilderFactory.get("csvReadStep").
        <Product,Product>chunk(2)
        .reader(flatFileItemReader)
        .processor(new ProductProcessor())
        .writer(new ConsoleItemWriter())
        .build();
  }

  @Bean
  Step jsonReadStep(StepBuilderFactory stepBuilderFactory,
                           JsonItemReader<Product> jsonItemReader) {
    return stepBuilderFactory.get("jsonReadStep").
        <Product,Product>chunk(1)
        .reader(jsonItemReader)
        .writer(new ConsoleItemWriter())
        .build();
  }

  @Bean
  Flow csvReadFlow(Step csvReadStep) {
    return new FlowBuilder<SimpleFlow>("csvReadFlow")
        .start(csvReadStep)
        .build();
  }

  @Bean
  Flow jsonReadFlow(Step jsonReadStep) {
    return new FlowBuilder<SimpleFlow>("jsonReadFlow")
        .start(jsonReadStep)
        .build();
  }

  @Bean
  Flow splitFlow(Flow csvReadFlow, Flow jsonReadFlow) {
    return new FlowBuilder<SimpleFlow>("splitFlow")
        .split(new SimpleAsyncTaskExecutor())
        .add(csvReadFlow, jsonReadFlow)
        .build();
  }

  @Bean
  TaskletStep cleanupStep(StepBuilderFactory stepBuilderFactory) {
    return stepBuilderFactory.get("cleanup")
        .tasklet(
            (contribution, chunkContext) -> {
              log.info("Performing cleanup...");
              return RepeatStatus.FINISHED;
            })
        .build();
  }

  @Bean
  Job flowExampleJob(JobBuilderFactory jobBuilderFactory, Flow splitFlow, Step cleanupStep){
    return jobBuilderFactory.get("flowExampleJob")
        .incrementer(new RunIdIncrementer())
        .start(splitFlow)
        .next(cleanupStep)
        .end()
        .build();
  }

}
