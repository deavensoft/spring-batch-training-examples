package com.deavensoft.springbatchtraining.testapp.model;

import lombok.Data;

@Data
public class BookDetails {
  private String bookName;

  private String bookFormat;

  private String publishingYear;

  private String bookISBN;
}
