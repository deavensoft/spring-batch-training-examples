package com.deavensoft.springbatchtraining.app.chunk.reader;

import java.util.Arrays;
import java.util.List;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

public class InMemoryReader extends AbstractItemStreamItemReader<Integer> {

  Integer[] intArray ={1,2,3,4,5,6,7,8,9,10};
  List<Integer> myList = Arrays.asList(intArray);

  int index =0;

  @Override
  public Integer read() {
    Integer nextItem = null;
    if ( index < myList.size()){
      nextItem = myList.get(index);
      index++;
    }else{
      index= 0;
    }

    return nextItem;
  }

}
