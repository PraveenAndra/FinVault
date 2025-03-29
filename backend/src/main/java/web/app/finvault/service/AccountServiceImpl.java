package web.app.finvault.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import web.app.finvault.dto.AccountDto;
import web.app.finvault.dto.ConvertDto;
import web.app.finvault.dto.TransferDto;
import web.app.finvault.entity.Account;
import web.app.finvault.entity.Transaction;
import web.app.finvault.entity.User;
import web.app.finvault.repository.AccountRepository;
import web.app.finvault.service.helper.AccountHelper;
import web.app.finvault.util.RandomUtil;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;
    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public Account createAccount(AccountDto accountDto, User user) {

        return accountHelper.createAccount(accountDto, user);

    }

    @Override
    public List<Account> getUserAccounts(String userId) {

        return accountRepository.findAllByOwnerUid(userId);

    }

    @Override
    public Account findAccount(String code, Long recipientAccountNumber) {
        logger.info("Search for account with code: {} and recipient account number: {}", code, recipientAccountNumber);
        return accountRepository.findByCodeAndAccountNumber(code, recipientAccountNumber).orElseThrow();

    }

    @Override
    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        logger.info("Currency Code: {}", transferDto.getCode());
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid())
                .orElseThrow(() -> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow();
        return accountHelper.performTransfer(senderAccount, receiverAccount, transferDto, user);
    }


    public Map<String, Double> getExchangeRates() {
        return exchangeRateService.getExchangeRates();
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        return accountHelper.convertCurrency(convertDto, user);
    }

}
