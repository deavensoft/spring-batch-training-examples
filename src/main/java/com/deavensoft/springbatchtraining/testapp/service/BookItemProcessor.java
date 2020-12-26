package com.deavensoft.springbatchtraining.testapp.service;


import com.deavensoft.springbatchtraining.testapp.model.Book;
import com.deavensoft.springbatchtraining.testapp.model.BookRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BookItemProcessor implements ItemProcessor<BookRecord, Book> {

  @Override
  public Book process(BookRecord item) throws Exception {
    Book book = new Book();
    book.setAuthor(item.getBookAuthor());
    book.setName(item.getBookName());
    log.info("Processing book {}", book);
    return book;
  }

}
