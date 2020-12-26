package com.deavensoft.springbatchtraining.testapp.model;

import lombok.Data;

@Data
public class BookRecord {
  private String bookName;

  private String bookAuthor;

  private String bookFormat;

  private String bookISBN;

  private String publishingYear;
}
