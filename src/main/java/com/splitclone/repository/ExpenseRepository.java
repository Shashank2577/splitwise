package com.splitclone.repository;

import com.splitclone.model.Expense;
import com.splitclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByPayerOrderByDateDesc(User payer);
    
    List<Expense> findByGroupIdOrderByDateDesc(Long groupId);
    
    @Query("SELECT e FROM Expense e JOIN e.splits s WHERE s.user = :user ORDER BY e.date DESC")
    List<Expense> findExpensesInvolvedByUser(@Param("user") User user);
    
    @Query("SELECT DISTINCT e FROM Expense e " +
           "LEFT JOIN e.splits s " +
           "WHERE e.payer = :user OR s.user = :user " +
           "ORDER BY e.date DESC")
    List<Expense> findAllExpensesForUser(@Param("user") User user);
}
