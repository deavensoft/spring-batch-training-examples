package com.deavensoft.springbatchtraining.app.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  private Integer productId;
  private String name;

  private BigDecimal price;
  private Integer unit;
  private String description;
}
