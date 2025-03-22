package com.eric.lbms.service;

import com.eric.lbms.model.Book;
import com.eric.lbms.model.BorrowRecord;
import com.eric.lbms.model.BorrowStatus;
import com.eric.lbms.model.User;
import com.eric.lbms.repository.BookRepository;
import com.eric.lbms.repository.BorrowRecordRepository;
import com.eric.lbms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowServiceTest {
    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowService borrowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_Success() {
        User user = new User();
        user.setId(1L);
        Book book = new Book();
        book.setId(1L);
        book.setAvailableCopies(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.countByUserIdAndStatus(1L, BorrowStatus.BORROWED)).thenReturn(0L);
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        BorrowRecord result = borrowService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(BorrowStatus.BORROWED, result.getStatus());
        verify(bookRepository).save(book);
    }
}
