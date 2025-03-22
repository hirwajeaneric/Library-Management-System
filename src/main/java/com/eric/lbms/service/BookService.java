package com.eric.lbms.service;

import com.eric.lbms.model.Book;
import com.eric.lbms.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;

    public Book createBook(Book book) {
        log.info("Create book: {}", book.getTitle());
        return bookRepository.save(book);
    }
}