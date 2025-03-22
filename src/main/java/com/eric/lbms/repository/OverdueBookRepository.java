package com.eric.lbms.repository;

import com.eric.lbms.model.OverdueBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OverdueBookRepository extends JpaRepository<OverdueBook, Long> {
}
