package com.splitclone.service;

import com.splitclone.dto.CreateExpenseRequest;
import com.splitclone.dto.ExpenseDTO;
import com.splitclone.model.Expense;
import com.splitclone.model.ExpenseSplit;
import com.splitclone.model.User;
import com.splitclone.repository.ExpenseRepository;
import com.splitclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Transactional
    public ExpenseDTO createExpense(CreateExpenseRequest request, User payer) {
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setPayer(payer);
        expense.setDate(LocalDateTime.now());
        expense.setCategory(request.getCategory());
        expense.setNotes(request.getNotes());

        // Handle splits
        BigDecimal totalAmount = request.getAmount();
        List<User> participants = userRepository.findAllById(request.getParticipantIds());

        if (request.getSplitType() == ExpenseSplit.SplitType.EQUAL) {
            BigDecimal splitAmount = totalAmount.divide(BigDecimal.valueOf(participants.size()), 2, BigDecimal.ROUND_HALF_UP);
            for (User participant : participants) {
                ExpenseSplit split = new ExpenseSplit();
                split.setExpense(expense);
                split.setUser(participant);
                split.setAmount(splitAmount);
                split.setSplitType(ExpenseSplit.SplitType.EQUAL);
                split.setPaid(participant.equals(payer));
                expense.getSplits().add(split);
            }
        } else if (request.getSplitType() == ExpenseSplit.SplitType.EXACT) {
            for (int i = 0; i < participants.size(); i++) {
                ExpenseSplit split = new ExpenseSplit();
                split.setExpense(expense);
                split.setUser(participants.get(i));
                split.setAmount(request.getSplitAmounts().get(i));
                split.setSplitType(ExpenseSplit.SplitType.EXACT);
                split.setPaid(participants.get(i).equals(payer));
                expense.getSplits().add(split);
            }
        }

        expense = expenseRepository.save(expense);
        return convertToDTO(expense);
    }

    public List<ExpenseDTO> getUserExpenses(User user) {
        return expenseRepository.findAllExpensesForUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExpenseDTO getExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return convertToDTO(expense);
    }

    @Transactional
    public void deleteExpense(Long id, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getPayer().equals(user)) {
            throw new RuntimeException("Not authorized to delete this expense");
        }

        expenseRepository.delete(expense);
    }

    private ExpenseDTO convertToDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setPayerId(expense.getPayer().getId());
        dto.setPayerName(expense.getPayer().getName());
        dto.setDate(expense.getDate());
        dto.setCategory(expense.getCategory());
        dto.setNotes(expense.getNotes());

        dto.setSplits(expense.getSplits().stream()
                .map(split -> new ExpenseDTO.SplitDTO(
                    split.getUser().getId(),
                    split.getUser().getName(),
                    split.getAmount(),
                    split.getSplitType(),
                    split.isPaid()
                ))
                .collect(Collectors.toList()));

        return dto;
    }
}
