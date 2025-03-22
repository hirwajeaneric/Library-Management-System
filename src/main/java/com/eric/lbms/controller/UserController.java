package com.eric.lbms.controller;

import com.eric.lbms.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final BorrowService borrowService;

    @GetMapping("/history")
    public ResponseEntity<?> getBorrowHistory(@RequestParam Long userId, Pageable pageable) {
        return ResponseEntity.ok(borrowService.getUserBorrowHistory(userId, pageable));
    }
}