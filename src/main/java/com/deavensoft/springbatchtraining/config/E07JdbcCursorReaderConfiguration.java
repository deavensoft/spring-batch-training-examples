package com.deavensoft.springbatchtraining.config;

import com.deavensoft.springbatchtraining.app.chunk.writer.ConsoleItemWriter;
import com.deavensoft.springbatchtraining.app.model.Product;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/* Requires populated product table in the DB */
@Configuration
@EnableBatchProcessing
@Slf4j
public class E07JdbcCursorReaderConfiguration {

  @StepScope
  @Bean
  JdbcCursorItemReader<Product> jdbcCursorItemReader(DataSource dataSource){
    JdbcCursorItemReader<Product> reader = new JdbcCursorItemReader<>();
    reader.setDataSource(dataSource);
    reader.setSql("select product_id, name, price, unit, description from product");
    reader.setRowMapper(new BeanPropertyRowMapper<>(){
      {
        setMappedClass(Product.class);
      }
    });
    return reader;
  }


  @Bean
  @SuppressWarnings("unchecked")
  Step step1(StepBuilderFactory stepBuilderFactory,
                    ItemReader<Product> jdbcCursorItemReader){
    return stepBuilderFactory.get("step1").
        <Product,Product>chunk(2)
        .reader(jdbcCursorItemReader)
        .writer(new ConsoleItemWriter())
        .build();
  }

  @Bean
  Job exampleJdbcCursorItemReaderJob(JobBuilderFactory jobBuilderFactory, Step step1){
    return jobBuilderFactory.get("exampleJdbcCursorItemReaderJob")
        .incrementer(new RunIdIncrementer())
        .start(step1)
        .build();
  }

}
