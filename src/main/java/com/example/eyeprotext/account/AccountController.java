package com.example.eyeprotext.account;

import com.example.eyeprotext.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/account")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    public GeneralResponse getAccountByEmailAndPassword(@RequestParam String email,
                                                        @RequestParam String password) {
        return accountService.getAccountByEmailAndPassword(email, password);
    }

    @PostMapping
    public GeneralResponse registerNewAccount(@RequestBody Account account) {
        return accountService.addNewAccount(account);
    }

    @DeleteMapping(path = "{accountId}")
    public void deleteAccount(@PathVariable("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
    }

    @PutMapping(path = "{accountId}")
    public void updateAccount(@PathVariable("accountId") Long accountId,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) {
        accountService.updateAccount(accountId, name, email);
    }
}
