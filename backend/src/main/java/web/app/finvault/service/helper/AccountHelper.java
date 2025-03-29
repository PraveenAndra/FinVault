package web.app.finvault.service.helper;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import web.app.finvault.dto.AccountDto;
import web.app.finvault.dto.ConvertDto;
import web.app.finvault.dto.TransferDto;
import web.app.finvault.entity.Account;
import web.app.finvault.entity.Transaction;
import web.app.finvault.entity.Type;
import web.app.finvault.entity.User;
import web.app.finvault.repository.AccountRepository;
import web.app.finvault.service.ExchangeRateService;
import web.app.finvault.service.TransactionService;
import web.app.finvault.util.RandomUtil;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ExchangeRateService exchangeRateService;
    private final Logger logger = LoggerFactory.getLogger(AccountHelper.class);

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "INR", "Indian Rupee"
    );


    public Account createAccount(AccountDto accountDto, User user) {

        long accountNumber;

        try{
            validateAccountNotExistsForUser(accountDto.getCode(), user.getUid());
        } catch (Exception e) {
            logger.error("Error validating account not exists for user: {}", e.getMessage());
        }

        do {
            accountNumber = new RandomUtil().generateRandomAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        var account =  Account.builder()
                .accountNumber(accountNumber)
                .accountName(user.getFirstname()+" "+user.getLastname())
                .code(accountDto.getCode())
                .symbol(accountDto.getSymbol())
                .label(CURRENCIES.get(accountDto.getCode()))
                .owner(user)
                .balance(0)
                .build();
        return accountRepository.save(account);

    }

    public void validateAccountNotExistsForUser(String code, String uid) throws Exception {

        if(accountRepository.existsByCodeAndOwnerUid(code, uid)) {
            throw new Exception("Account with this code already exists for this user");
        }
    }
    public void validateSufficientFunds(Account account, double amount) throws Exception {
        if(account.getBalance() < amount) {
            throw new OperationNotSupportedException("Insufficient funds in the account");
        }
    }

    public void validateAmount(double amount) throws Exception {
        if(amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, TransferDto transferDto, User user) throws Exception {
        double amount = transferDto.getAmount();
        // Implementation of transfer logic goes here
        validateSufficientFunds(senderAccount, (amount * 1.01));
        senderAccount.setBalance(senderAccount.getBalance() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));
        var senderTransaction = transactionService.createAccountTransaction(amount,transferDto.getDescription(), Type.WITHDRAW, amount * 0.01, user, senderAccount);
        var receiverTransaction = transactionService.createAccountTransaction(amount,transferDto.getDescription(), Type.DEPOSIT, 0.00, receiverAccount.getOwner(), receiverAccount);

        return senderTransaction;
    }

    public void validateDifferentCurrencyType(ConvertDto convertDto) throws Exception {
        if(convertDto.getToCurrencyCode().equals(convertDto.getFromCurrencyCode())){
            throw new IllegalArgumentException("Conversion between the same currency types is not allowed");
        }
    }

    public void validateAccountOwnership(ConvertDto convertDto, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrencyCode(), uid).orElseThrow();
        accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrencyCode(), uid).orElseThrow();
    }

    public void validateAccountOwnership(String code, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(code, uid).orElseThrow();
    }

    private void validateConversion(ConvertDto convertDto, String uid) throws Exception {
        validateDifferentCurrencyType(convertDto);
        validateAccountOwnership(convertDto, uid);
        validateAmount(convertDto.getAmount());
        Account senderAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrencyCode(), uid)
                .orElseThrow(() -> new Exception("Account not found"));
        validateSufficientFunds(senderAccount, convertDto.getAmount());

    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        validateConversion(convertDto, user.getUid());
        var rates = exchangeRateService.getExchangeRates();
        var sendingRates = rates.get(convertDto.getFromCurrencyCode());
        var receivingRates = rates.get(convertDto.getToCurrencyCode());
        var computedAmount = (receivingRates/sendingRates) * convertDto.getAmount();
        Account fromAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrencyCode(), user.getUid()).orElseThrow();
        Account toAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrencyCode(), user.getUid()).orElseThrow();

        fromAccount.setBalance(fromAccount.getBalance() - convertDto.getAmount());
        toAccount.setBalance(toAccount.getBalance() + computedAmount);
        accountRepository.saveAll(List.of(fromAccount, toAccount));

        var fromAccountTransaction = transactionService.createAccountTransaction(convertDto.getAmount(), "Converted", Type.CONVERSION, convertDto.getAmount() * 0.01, user, fromAccount);
        var toAccountTransaction = transactionService.createAccountTransaction(computedAmount, "Converted", Type.DEPOSIT, 0.00, user, toAccount);

        return fromAccountTransaction;
    }


}
