package com.eric.lbms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Table(name="overdue_books")
@Immutable
@Data
public class OverdueBook {
    @Id
    private Long recordId;
    private Long userId;
    private String userName;
    private Long bookId;
    private String title;
    private LocalDateTime dueDate;
}
