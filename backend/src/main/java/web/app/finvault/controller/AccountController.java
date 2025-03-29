package web.app.finvault.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import web.app.finvault.dto.AccountDto;
import web.app.finvault.dto.ConvertDto;
import web.app.finvault.dto.TransferDto;
import web.app.finvault.entity.Account;
import web.app.finvault.entity.Transaction;
import web.app.finvault.entity.User;
import web.app.finvault.service.AccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @PostMapping("/create"  )
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        logger.info("Creating account for user: {}", user.getUid());

        return ResponseEntity.ok(accountService.createAccount(accountDto, user));


    }

    @GetMapping("/fetchAccounts")
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }

    @PostMapping("/convert")
    public ResponseEntity<Transaction> convertCurrency(@RequestBody ConvertDto convertDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));
    }

}
