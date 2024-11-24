package com.splitclone.dto;

import com.splitclone.model.Expense;
import com.splitclone.model.ExpenseSplit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateExpenseRequest {
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Category is required")
    private Expense.ExpenseCategory category;

    @NotNull(message = "Split type is required")
    private ExpenseSplit.SplitType splitType;

    @NotNull(message = "Participant IDs are required")
    private List<Long> participantIds;

    private List<BigDecimal> splitAmounts;

    private Long groupId;

    private String notes;
}
