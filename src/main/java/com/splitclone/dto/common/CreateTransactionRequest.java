package com.splitclone.dto.common;

import com.splitclone.model.common.Share;
import com.splitclone.model.common.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateTransactionRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private Transaction.TransactionType type;

    @NotNull(message = "Share type is required")
    private Share.ShareType shareType;

    @NotNull(message = "Participant IDs are required")
    private List<Long> participantIds;

    private List<BigDecimal> shareAmounts;

    private Long groupId;

    private String notes;
}
