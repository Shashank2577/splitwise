package com.splitclone.dto;

import com.splitclone.model.Expense;
import com.splitclone.model.ExpenseSplit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpenseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private Long payerId;
    private String payerName;
    private LocalDateTime date;
    private Expense.ExpenseCategory category;
    private String notes;
    private String receiptUrl;
    private List<SplitDTO> splits;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SplitDTO {
        private Long userId;
        private String userName;
        private BigDecimal amount;
        private ExpenseSplit.SplitType splitType;
        private boolean paid;
    }
}
