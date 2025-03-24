package web.app.finvault.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import web.app.finvault.dto.AccountDto;
import web.app.finvault.entity.Account;
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
}
