package web.app.finvault.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import web.app.finvault.entity.*;
import web.app.finvault.repository.TransactionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAllTransactionsForUser(User user, String page) {
        // Implementation of getting transactions for a user goes here
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByOwnerUid(user.getUid(), pageable).getContent();
    }

    @Override
    public Transaction createAccountTransaction(double amount, String description, Type type, double transactionFee, User user, Account account) {
        var transaction = Transaction.builder()
                .transactionFee(transactionFee)
                .amount(amount)
                .type(type)
                .account(account)
                .owner(user)
                .status(Status.COMPLETED)
                .description(description)
                .build();
        return transactionRepository.save(transaction);
    }
}
