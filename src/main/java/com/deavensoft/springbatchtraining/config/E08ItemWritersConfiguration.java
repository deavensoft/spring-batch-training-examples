package com.deavensoft.springbatchtraining.config;

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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

/* Requires empty product table in the DB */
@Configuration
@EnableBatchProcessing
@Slf4j
public class E08ItemWritersConfiguration {

  private static final String INPUT_FILE_PATH = "input/product-bigfile.csv";
  private static final String OUTPUT_PATH = "/tmp/output.csv";

  @StepScope
  @Bean
  FlatFileItemReader flatFileItemReader(){

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
  @StepScope
  FlatFileItemWriter<Product> flatFileItemWriter(){
    FlatFileItemWriter<Product> writer = new FlatFileItemWriter<>();

    log.info("Writting data into: {}", OUTPUT_PATH);

    writer.setResource(new FileSystemResource(OUTPUT_PATH));
    writer.setLineAggregator( new DelimitedLineAggregator<>(){
      {
        setDelimiter("|");
        setFieldExtractor(new BeanWrapperFieldExtractor<>(){
          {
            setNames(new String[]{"productId","name","price","unit","description"});
          }
        });
      }
    });

    // how to write the header
    writer.setHeaderCallback(
        csvWriter -> csvWriter.write("productID|productName|price|unit|ProductDesc"));

    writer.setAppendAllowed(false);

    return writer;
  }


  @Bean
  JdbcBatchItemWriter<Product> dbWriter(DataSource dataSource){
    log.info("Writing to DB table...");
    return new JdbcBatchItemWriterBuilder<Product>()
        .dataSource(dataSource)
        .sql("insert into product (product_id, name, price, unit, description)" +
            " values ( :productId, :name, :price, :unit, :description ) ")
        .beanMapped()
        .build();
  }

  @Bean
  @SuppressWarnings("unchecked")
  Step step1(StepBuilderFactory stepBuilderFactory,
                    ItemReader<Product> flatFileItemReader,
                    ItemWriter<Product> flatFileItemWriter,
                    ItemWriter<Product> dbWriter){
    return stepBuilderFactory.get("step1").
        <Product,Product>chunk(2)
        .reader(flatFileItemReader)
//        .writer(flatFileItemWriter)
        .writer(dbWriter)
        .build();
  }

  @Bean
  Job readWriteJob(JobBuilderFactory jobBuilderFactory, Step step1){
    return jobBuilderFactory.get("readWriteJob")
        .incrementer(new RunIdIncrementer())
        .start(step1)
        .build();
  }

}
