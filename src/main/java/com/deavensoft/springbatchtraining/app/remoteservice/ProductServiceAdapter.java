package com.deavensoft.springbatchtraining.app.remoteservice;

import com.deavensoft.springbatchtraining.app.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductServiceAdapter {

  private int counter;
  private final List<Product> productList;

  public ProductServiceAdapter() {
    this.productList = initProducts();
  }

  private List<Product> initProducts() {
    List<Product> list = new ArrayList<>();
    list.add(Product.builder().productId(201).name("Product 201").price(new BigDecimal("500.5")).build());
    list.add(null);
    list.add(Product.builder().productId(202).name("Product 202").price(new BigDecimal("600.5")).build());
    list.add(null);
    list.add(null);
    list.add(Product.builder().productId(203).name("Product 203").price(new BigDecimal("700.5")).build());
    list.add(Product.builder().productId(204).name("Product 204").price(new BigDecimal("800.5")).build());
    list.add(Product.builder().productId(205).name("Product 205").price(new BigDecimal("900.5")).build());
    return list;
  }

  public Product nextProduct() throws InterruptedException {

    Product p = null;
    Thread.sleep(1000);
    try {
      p = getProductFromRemoteService();
      log.info("connected web service .... ok");
    }catch(Exception e){
      log.error("exception ... {}", e.getMessage());
      throw e;
    }
    return p;
  }

  private Product getProductFromRemoteService() {
    if (counter >= this.productList.size()) {
      return null;
    }

    Product product = this.productList.get(counter++);
    if (product != null) {
      return product;
    } else {
      throw new RemoteServiceException("Problem calling remote web service for counter: " + (counter - 1));
    }
  }

}
