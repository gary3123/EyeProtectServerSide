package com.example.eyeprotext.account;

import com.example.eyeprotext.APNsPushy.APNsPushNotification;
import com.example.eyeprotext.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<AuthenticationResponse> LoginAccount(@RequestBody Account account) {
        String deviceToken = "AEC96811A66E9E0555BD49E7459619004C4A8D05E0511ECCFFCE9F27148987A7";
        String msgBody = "{ aps: { alert: Hello } }";
        APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
        return ResponseEntity.ok(accountService.loginAccount(account));
    }

    @PostMapping(path = "/register")
    public GeneralResponse registerNewAccount(@RequestBody Account account) {
        return accountService.addNewAccount(account);
    }

//    @PostMapping(path = "/register")
//    public GeneralResponse registerNewAccount(@RequestBody Account account) {
//        return accountService.addNewAccount(account);
//    }

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

    @PostMapping(path = "/uploadImage")
    public GeneralResponse uploadImage(@RequestBody Account account) {
        return accountService.uploadImages(account.getAccountId(), account.getImage());
    }

    @PostMapping(path = "/getAccountPersonInformation")
    public GeneralResponse getAccountPersonInformation(@RequestBody Account account) {
        return accountService.getAccountPersonInformation(account.getAccountId());
    }

    @PostMapping(path = "/getFriendList")
    public GeneralResponse getFriendList(@RequestBody Account account) {
        return  accountService.getFriendList(account.getAccountId());
    }
}
