package com.example.eyeprotext.account;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.request.AddFriendInviteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(path = "/logout")
    public GeneralResponse logout(@RequestBody Account account) {
        return accountService.logout(account.getAccountId());
    }

    @PostMapping(path = "/addFriendInvite")
    public GeneralResponse addFriendInvite(@RequestBody AddFriendInviteRequest request) {
        return accountService.addFriendInvite(request);
    }

    @PostMapping(path = "/getFriendInviteList")
    public GeneralResponse getFriendInviteList(@RequestBody Account account) {
        return accountService.getFriendInviteList(account);
    }
}
