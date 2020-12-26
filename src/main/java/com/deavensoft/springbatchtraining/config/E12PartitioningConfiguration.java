package com.deavensoft.springbatchtraining.config;

import com.deavensoft.springbatchtraining.app.chunk.processor.ProductSkipListener;
import com.deavensoft.springbatchtraining.app.chunk.writer.ConsoleItemWriter;
import com.deavensoft.springbatchtraining.app.model.Product;
import com.deavensoft.springbatchtraining.app.partitioning.ColumnRangePartitioner;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/* Requires populated product table in the DB */
@Configuration
@EnableBatchProcessing
@Slf4j
public class E12PartitioningConfiguration {

  @Bean
  @StepScope
  JdbcPagingItemReader<Product> pagingItemReader(
      DataSource dataSource,
      @Value("#{stepExecutionContext['minValue']}") Long minValue,
      @Value("#{stepExecutionContext['maxValue']}") Long maxValue
  ){
    log.info("From " + minValue + "to "+ maxValue );
    Map<String, Order> sortKey = new HashMap<>();
    sortKey.put("product_id", Order.ASCENDING);

    MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
    queryProvider.setSelectClause("product_id, name, description, unit, price");
    queryProvider.setFromClause("from product");
    queryProvider.setWhereClause("where product_id >=" + minValue + " and product_id <" + maxValue);
    queryProvider.setSortKeys(sortKey);

    JdbcPagingItemReader reader = new JdbcPagingItemReader();
    reader.setDataSource(dataSource);
    reader.setQueryProvider(queryProvider);
    reader.setFetchSize(1000);

    reader.setRowMapper(new BeanPropertyRowMapper<Product>(){
      {
        setMappedClass(Product.class);
      }
    });

    return reader;

  }

  @Bean
  ColumnRangePartitioner columnRangePartitioner(DataSource dataSource){
    ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();
    columnRangePartitioner.setColumn("product_id");;
    columnRangePartitioner.setDataSource(dataSource);
    columnRangePartitioner.setTable("product");
    return columnRangePartitioner;
  }

  @Bean
  Step partitionStep(StepBuilderFactory stepBuilderFactory, Step slaveStep,
      ColumnRangePartitioner columnRangePartitioner){
    return stepBuilderFactory.get("partitionStep")
        .partitioner(slaveStep.getName(), columnRangePartitioner)
        .step(slaveStep)
        .gridSize(5)
        .taskExecutor(new SimpleAsyncTaskExecutor())
        .build();
  }

  @Bean
  Step slaveStep(StepBuilderFactory stepBuilderFactory,
      JdbcPagingItemReader<Product> pagingItemReader){
    return stepBuilderFactory.get("slaveStep")
        .<Product,Product>chunk(5)
        .reader(pagingItemReader)
        .writer(new ConsoleItemWriter())
        .build();
  }


  @Bean
  Job partitioningJob(JobBuilderFactory jobBuilderFactory, Step partitionStep){
    return jobBuilderFactory.get("partitioningJob")
        .incrementer(new RunIdIncrementer())
        .start(partitionStep)
        .build();
  }



}
