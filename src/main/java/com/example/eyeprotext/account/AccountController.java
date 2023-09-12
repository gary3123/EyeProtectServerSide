package com.example.eyeprotext.account;

import com.example.eyeprotext.APNsPushy.APNsPushNotification;
import com.example.eyeprotext.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/account")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public GeneralResponse getAccountByEmailAndPassword(@RequestBody Account account) {
        String deviceToken = "AEC96811A66E9E0555BD49E7459619004C4A8D05E0511ECCFFCE9F27148987A7";
        String msgBody = "{ aps: { alert: Hello } }";
        APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
        return accountService.getAccountByEmailAndPassword(account.getEmail(), account.getPassword(), account.getDeviceToken());
    }

    @PostMapping(path = "/register")
    public GeneralResponse registerNewAccount(@RequestBody Account account) {
        return accountService.addNewAccount(account);
    }

    @DeleteMapping(path = "{accountId}")
    public void deleteAccount(@PathVariable("accountId") UUID accountId) {
        accountService.deleteAccount(accountId);
    }

    @PutMapping(path = "{accountId}")
    public void updateAccount(@PathVariable("accountId") UUID accountId,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) {
        accountService.updateAccount(accountId, name, email);
    }
}
