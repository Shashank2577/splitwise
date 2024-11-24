package com.splitclone.dto.common;

import com.splitclone.model.common.Share;
import com.splitclone.model.common.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionDTO {
    private Long id;
    private String title;
    private BigDecimal amount;
    private Long creatorId;
    private String creatorName;
    private LocalDateTime createdAt;
    private Transaction.TransactionType type;
    private String notes;
    private String attachmentUrl;
    private List<ShareDTO> shares;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareDTO {
        private Long userId;
        private String userName;
        private BigDecimal amount;
        private Share.ShareType type;
        private boolean settled;
    }
}
