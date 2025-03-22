package com.eric.lbms.service;

import com.eric.lbms.model.Book;
import com.eric.lbms.model.BorrowRecord;
import com.eric.lbms.model.BorrowStatus;
import com.eric.lbms.model.User;
import com.eric.lbms.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBorrowSummaryRepository userBorrowSummaryRepository;
    private final OverdueBookRepository overdueBookRepository;

    @Transactional
    public BorrowRecord borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        long activeBorrows = borrowRecordRepository.countByUserIdAndStatus(user.getId(), BorrowStatus.BORROWED);
        if (activeBorrows >= 3) {
            throw new IllegalStateException("User has reached maximum borrow limit of 3 books");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book has no available copies");
        }

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusWeeks(2));
        record.setStatus(BorrowStatus.BORROWED);

        book.setAvailableCopies(book.getAvailableCopies() -1);
        bookRepository.save(book);

        log.info("User {} borrowed book {}", user.getId(), book.getId());
        return borrowRecordRepository.save(record);
    }

    @Transactional
    public BorrowRecord returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Borrow record not found"));

        if (record.getStatus() != BorrowStatus.BORROWED) {
            throw new IllegalStateException("Book is not currently borrowed");
        }

        record.setReturnDate(LocalDateTime.now());
        record.setStatus(record.getDueDate().isBefore(LocalDateTime.now()) ? BorrowStatus.OVERDUE : BorrowStatus.RETURNED);

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        log.info("Book {} returned by user {}", book.getId(), record.getUser().getUsername());
        return borrowRecordRepository.save(record);
    }


}