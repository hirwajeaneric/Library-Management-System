package com.eric.lbms;

import com.eric.lbms.model.Book;
import com.eric.lbms.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LibraryApplicationTests {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testBookCreation() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setTotalCopies(5);

        Book savedBook = bookRepository.save(book);
        assertNotNull(savedBook.getId());
    }
}
