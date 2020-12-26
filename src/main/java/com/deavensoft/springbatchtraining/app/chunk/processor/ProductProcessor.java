package com.deavensoft.springbatchtraining.app.chunk.processor;

import com.deavensoft.springbatchtraining.app.model.Product;
import org.springframework.batch.item.ItemProcessor;

public class ProductProcessor implements ItemProcessor<Product, Product> {
  @Override
  public Product process(Product item) throws Exception {
    if (item.getProductId() == 2) {
      return null;
    } else {
      item.setDescription(item.getDescription().toUpperCase());
    }
    return item;
  }


}
