package com.eric.lbms.controller;

import com.eric.lbms.model.Book;
import com.eric.lbms.service.BookService;
import com.eric.lbms.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
public class LibrarianController {
    private final BookService bookService;
    private final BorrowService borrowService;

    @PostMapping("/books")
    public ResponseEntity<?> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return ResponseEntity.ok(borrowService.borrowBook(userId, bookId));
    }

    @PostMapping("/return/{recordId}")
    public ResponseEntity<?> returnBook(@PathVariable Long recordId) {
        return ResponseEntity.ok(borrowService.returnBook(recordId));
    }

    @GetMapping("/borrow-summary")
    public ResponseEntity<?> getBorrowSummary() {
        return ResponseEntity.ok(borrowService.getBorrowSummary());
    }

    @GetMapping("/overdue-books")
    public ResponseEntity<?> getOverdueBooks() {
        return ResponseEntity.ok(borrowService.getOverdueBooks());
    }
}