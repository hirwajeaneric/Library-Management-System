package com.eric.lbms.repository;

import com.eric.lbms.model.UserBorrowSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBorrowSummaryRepository extends JpaRepository<UserBorrowSummary, Long> {
}
