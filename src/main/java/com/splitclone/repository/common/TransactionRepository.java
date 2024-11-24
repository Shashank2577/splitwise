package com.splitclone.repository.common;

import com.splitclone.model.User;
import com.splitclone.model.common.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCreatorOrderByCreatedAtDesc(User creator);
    
    List<Transaction> findByGroupIdOrderByCreatedAtDesc(Long groupId);
    
    @Query("SELECT t FROM Transaction t JOIN t.shares s WHERE s.user = :user ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsInvolvingUser(@Param("user") User user);
    
    @Query("SELECT DISTINCT t FROM Transaction t " +
           "LEFT JOIN t.shares s " +
           "WHERE t.creator = :user OR s.user = :user " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findAllTransactionsForUser(@Param("user") User user);
}
