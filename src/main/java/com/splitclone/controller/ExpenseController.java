package com.splitclone.controller;

import com.splitclone.dto.CreateExpenseRequest;
import com.splitclone.dto.ExpenseDTO;
import com.splitclone.model.User;
import com.splitclone.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(
            @RequestPart("expense") CreateExpenseRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expenseService.createExpense(request, user));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getUserExpenses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expenseService.getUserExpenses(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpense(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        expenseService.deleteExpense(id, user);
        return ResponseEntity.ok().build();
    }
}
