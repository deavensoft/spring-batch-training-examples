package com.deavensoft.springbatchtraining.app.chunk.writer;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

@Slf4j
@SuppressWarnings("unchecked")
public class ConsoleItemWriter extends AbstractItemStreamItemWriter {

  @Override
  public void write(List items) throws Exception {
    items.forEach(obj -> log.info(obj.toString()));
    log.info(" ************ writing each chunk ***********");
  }
}
