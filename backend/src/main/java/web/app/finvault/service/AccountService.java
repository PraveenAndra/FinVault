package web.app.finvault.service;

import web.app.finvault.dto.AccountDto;
import web.app.finvault.dto.ConvertDto;
import web.app.finvault.dto.TransferDto;
import web.app.finvault.entity.Account;
import web.app.finvault.entity.Transaction;
import web.app.finvault.entity.User;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountDto accountDto, User user);
    List<Account> getUserAccounts(String userId);
    Account findAccount(String code, Long recipientAccountNumber);
    Transaction transferFunds(TransferDto transferDto, User user) throws Exception;


    Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception;
}
