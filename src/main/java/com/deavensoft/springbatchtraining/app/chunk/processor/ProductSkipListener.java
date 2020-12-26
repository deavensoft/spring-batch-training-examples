package com.deavensoft.springbatchtraining.app.chunk.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;

@Slf4j
public class ProductSkipListener {
  @OnSkipInRead
  public void onSkipRead(Throwable t){
    if ( t instanceof FlatFileParseException){
      FlatFileParseException ffep = (FlatFileParseException) t;
      log.warn("Product skipped: {}", ffep.getInput());
    }
  }

}
