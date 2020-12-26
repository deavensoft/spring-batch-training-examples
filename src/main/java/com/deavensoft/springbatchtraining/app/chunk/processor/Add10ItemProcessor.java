package com.deavensoft.springbatchtraining.app.chunk.processor;

import org.springframework.batch.item.ItemProcessor;

public class Add10ItemProcessor implements ItemProcessor<Integer, Integer> {

  @Override
  public Integer process(Integer item) {
    return Integer.sum(10, item);
  }
}

