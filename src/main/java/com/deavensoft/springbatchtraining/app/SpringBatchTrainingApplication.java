package com.deavensoft.springbatchtraining.app;

import com.deavensoft.springbatchtraining.config.E01HelloWorldJobConfiguration;
import com.deavensoft.springbatchtraining.config.E02HelloWorldWithIncrementerJobConfiguration;
import com.deavensoft.springbatchtraining.config.E03TaskletJobConfiguration;
import com.deavensoft.springbatchtraining.config.E04JobStepListenerConfiguration;
import com.deavensoft.springbatchtraining.config.E05ChunkProcessingConfiguration;
import com.deavensoft.springbatchtraining.config.E06FlatFileItemReaderConfiguration;
import com.deavensoft.springbatchtraining.config.E07JdbcCursorReaderConfiguration;
import com.deavensoft.springbatchtraining.config.E08ItemWritersConfiguration;
import com.deavensoft.springbatchtraining.config.E09ItemProcessorConfiguration;
import com.deavensoft.springbatchtraining.config.E10IFlowStepsConfiguration;
import com.deavensoft.springbatchtraining.config.E11SkipConfiguration;
import com.deavensoft.springbatchtraining.config.E12PartitioningConfiguration;
import com.deavensoft.springbatchtraining.config.E14SpringCloudDataFlowConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Import;


@SpringBootApplication
//@Import(E01HelloWorldJobConfiguration.class)
//@Import(E02HelloWorldWithIncrementerJobConfiguration.class)
//@Import(E03TaskletJobConfiguration.class)
//@Import(E04JobStepListenerConfiguration.class)
//@Import(E05ChunkProcessingConfiguration.class)
//@Import(E06FlatFileItemReaderConfiguration.class) // with parameters
//@Import(E07JdbcCursorReaderConfiguration.class)
//@Import(E08ItemWritersConfiguration.class)
//@Import(E09ItemProcessorConfiguration.class)
//@Import(E10IFlowStepsConfiguration.class)
//@Import(E11SkipConfiguration.class)
//@Import(E12PartitioningConfiguration.class)
@Import(E14SpringCloudDataFlowConfiguration.class)
@EnableTask
public class SpringBatchTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchTrainingApplication.class, args);
	}

}
