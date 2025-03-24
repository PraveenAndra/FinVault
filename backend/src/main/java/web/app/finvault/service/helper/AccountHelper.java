package web.app.finvault.service.helper;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import web.app.finvault.dto.AccountDto;
import web.app.finvault.entity.Account;
import web.app.finvault.entity.User;
import web.app.finvault.repository.AccountRepository;
import web.app.finvault.util.RandomUtil;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccountHelper {

    private final AccountRepository accountRepository;
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


}
