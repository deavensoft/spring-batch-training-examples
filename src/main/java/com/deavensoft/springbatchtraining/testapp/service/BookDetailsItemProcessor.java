package com.deavensoft.springbatchtraining.testapp.service;

import com.deavensoft.springbatchtraining.testapp.model.BookDetails;
import com.deavensoft.springbatchtraining.testapp.model.BookRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BookDetailsItemProcessor implements ItemProcessor<BookRecord, BookDetails> {

  @Override
  public BookDetails process(BookRecord item) throws Exception {
    BookDetails bookDetails = new BookDetails();
    bookDetails.setBookFormat(item.getBookFormat());
    bookDetails.setBookISBN(item.getBookISBN());
    bookDetails.setPublishingYear(item.getPublishingYear());
    bookDetails.setBookName(item.getBookName());
    log.info("Processing bookdetails {}", bookDetails);
    return bookDetails;
  }

}
