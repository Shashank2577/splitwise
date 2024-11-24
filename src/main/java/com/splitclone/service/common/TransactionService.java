package com.splitclone.service.common;

import com.splitclone.dto.common.CreateTransactionRequest;
import com.splitclone.dto.common.TransactionDTO;
import com.splitclone.model.User;
import com.splitclone.model.common.Share;
import com.splitclone.model.common.Transaction;
import com.splitclone.repository.UserRepository;
import com.splitclone.repository.common.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public TransactionDTO createTransaction(CreateTransactionRequest request, User creator) {
        Transaction transaction = new Transaction();
        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setCreator(creator);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setType(request.getType());
        transaction.setNotes(request.getNotes());

        // Handle shares
        BigDecimal totalAmount = request.getAmount();
        List<User> participants = userRepository.findAllById(request.getParticipantIds());

        if (request.getShareType() == Share.ShareType.EQUAL) {
            BigDecimal shareAmount = totalAmount.divide(BigDecimal.valueOf(participants.size()), 2, BigDecimal.ROUND_HALF_UP);
            for (User participant : participants) {
                Share share = new Share();
                share.setTransaction(transaction);
                share.setUser(participant);
                share.setAmount(shareAmount);
                share.setType(Share.ShareType.EQUAL);
                share.setSettled(participant.equals(creator));
                transaction.getShares().add(share);
            }
        } else if (request.getShareType() == Share.ShareType.FIXED) {
            for (int i = 0; i < participants.size(); i++) {
                Share share = new Share();
                share.setTransaction(transaction);
                share.setUser(participants.get(i));
                share.setAmount(request.getShareAmounts().get(i));
                share.setType(Share.ShareType.FIXED);
                share.setSettled(participants.get(i).equals(creator));
                transaction.getShares().add(share);
            }
        }

        transaction = transactionRepository.save(transaction);
        return convertToDTO(transaction);
    }

    public List<TransactionDTO> getUserTransactions(User user) {
        return transactionRepository.findAllTransactionsForUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDTO(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getCreator().equals(user)) {
            throw new RuntimeException("Not authorized to delete this transaction");
        }

        transactionRepository.delete(transaction);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTitle(transaction.getTitle());
        dto.setAmount(transaction.getAmount());
        dto.setCreatorId(transaction.getCreator().getId());
        dto.setCreatorName(transaction.getCreator().getName());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setType(transaction.getType());
        dto.setNotes(transaction.getNotes());

        dto.setShares(transaction.getShares().stream()
                .map(share -> new TransactionDTO.ShareDTO(
                    share.getUser().getId(),
                    share.getUser().getName(),
                    share.getAmount(),
                    share.getType(),
                    share.isSettled()
                ))
                .collect(Collectors.toList()));

        return dto;
    }
}
