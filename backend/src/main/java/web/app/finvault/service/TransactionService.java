package web.app.finvault.service;

import web.app.finvault.entity.Account;
import web.app.finvault.entity.Transaction;
import web.app.finvault.entity.Type;
import web.app.finvault.entity.User;

import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactionsForUser(User user, String page);
    Transaction createAccountTransaction(double amount, String description, Type type, double transactionFee, User user, Account account);
}
