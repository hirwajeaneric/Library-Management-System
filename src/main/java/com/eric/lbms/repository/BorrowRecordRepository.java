package com.eric.lbms.repository;

import com.eric.lbms.model.BorrowRecord;
import com.eric.lbms.model.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    Page<BorrowRecord> findByUserId(Long userId, Pageable pageable);
    long countByUserIdAndStatus(Long userId, BorrowStatus status);
}