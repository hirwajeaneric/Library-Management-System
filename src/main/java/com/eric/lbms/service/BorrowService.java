package com.eric.lbms.service;

import com.eric.lbms.model.*;
import com.eric.lbms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    public Page<BorrowRecord> getUserBorrowHistory(Long userId, Pageable pageable) {
        log.info("User {} borrow history", userId);
        return borrowRecordRepository.findByUserId(userId, pageable);
    }

    public List<UserBorrowSummary> getBorrowSummary() {
        log.info("Fetching borrow summary for all users");
        return userBorrowSummaryRepository.findAll();
    }

    public List<OverdueBook> getOverdueBooks() {
        log.info("Fetching overdue books for all users");
        return overdueBookRepository.findAll();
    }
}