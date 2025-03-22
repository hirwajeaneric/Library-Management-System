package com.eric.lbms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "user_borrow_summary")
@Immutable
@Data
public class UserBorrowSummary {
    @Id
    private Long userId;
    private String username;
    private Long borrowedCount;
}
