package com.deavensoft.springbatchtraining.config;

import com.deavensoft.springbatchtraining.app.chunk.processor.ProductProcessor;
import com.deavensoft.springbatchtraining.app.chunk.processor.ProductSkipListener;
import com.deavensoft.springbatchtraining.app.chunk.writer.ConsoleItemWriter;
import com.deavensoft.springbatchtraining.app.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
@Slf4j
public class E11SkipConfiguration {

  private static final String INPUT_FILE_PATH = "input/productsWithErrors.csv";

  @StepScope
  @Bean
  public FlatFileItemReader flatFileItemReader(){

    FlatFileItemReader<Product> reader = new FlatFileItemReader<>();

    // 1. let reader know where is the file
    ClassPathResource inputResource = new ClassPathResource(INPUT_FILE_PATH);
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

  @Bean
  @SuppressWarnings("unchecked")
  public Step step1(StepBuilderFactory stepBuilderFactory,
                    ItemReader<Product> flatFileItemReader){
    return stepBuilderFactory.get("step1").
        <Product,Product>chunk(2)
        .reader(flatFileItemReader)
        .writer(new ConsoleItemWriter())
        .faultTolerant()
        .skip(FlatFileParseException.class)
        .skipLimit(1)
        .listener(new ProductSkipListener())
        .build();
  }

  @Bean
  public Job exampleSkipJob(JobBuilderFactory jobBuilderFactory, Step step1){
    return jobBuilderFactory.get("exampleSkipJob")
        .start(step1)
        .build();
  }



}
