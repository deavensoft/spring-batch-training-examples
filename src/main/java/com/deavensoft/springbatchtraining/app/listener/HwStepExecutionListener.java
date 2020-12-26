package com.deavensoft.springbatchtraining.app.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class HwStepExecutionListener  implements StepExecutionListener {

  @Override
  public void beforeStep(StepExecution stepExecution) {

    log.info("@@@ this is from Before Step Execution" + stepExecution.getJobExecution().getExecutionContext());

    log.info("@@@ In side Step - print job paramters" + stepExecution.getJobExecution().getJobParameters());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info("@@@ this is from After Step Execution" + stepExecution.getJobExecution().getExecutionContext());
    return null;
  }
}

