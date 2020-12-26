package com.deavensoft.springbatchtraining.app.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;


@Slf4j
public class HwJobExecutionListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info("!!! before starting the Job - Job Name:" + jobExecution.getJobInstance().getJobName());
    log.info("!!! before starting the Job" + jobExecution.getExecutionContext().toString());

    jobExecution.getExecutionContext().put("my company", "deavensoft");
    log.info("!!! before starting the Job - after set" + jobExecution.getExecutionContext().toString());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info("!!! after starting the Job - Job Execution Context" + jobExecution.getExecutionContext().toString());

  }
}

