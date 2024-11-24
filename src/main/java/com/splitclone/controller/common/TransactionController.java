package com.splitclone.controller.common;

import com.splitclone.dto.common.CreateTransactionRequest;
import com.splitclone.dto.common.TransactionDTO;
import com.splitclone.model.User;
import com.splitclone.service.common.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestPart("transaction") CreateTransactionRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.createTransaction(request, user));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(transactionService.getUserTransactions(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(id, user);
        return ResponseEntity.ok().build();
    }
}
