package com.example.eyeprotext.account;

import com.example.eyeprotext.GeneralResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccount() {
        return accountRepository.findAll();
    }

    public GeneralResponse addNewAccount(Account account) {
        Optional<Account> accountByEmail =
                accountRepository.findAccountByEmail(account.getEmail());
        if (accountByEmail.isPresent()) {
            return GeneralResponse.builder().message("faild").data("註冊到重複的帳號密碼").result(1).build();
        }
        accountRepository.save(account);
        return GeneralResponse.builder().message("susses").data("註冊成功").result(0).build();
    }

    public void deleteAccount(Long accountId) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException("account with id" + accountId + "does not exists");
        }
        accountRepository.deleteById(accountId);
    }
    @Transactional
    public void updateAccount(Long accountId,
                              String name,
                              String email) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalStateException("account with" +  accountId + "does not exists")
        );

        if (name != null && name.length() > 0 && !Objects.equals(account.getName(), name)) {
            account.setName(name);
        }

        if (email != null && name.length() > 0 && !Objects.equals(account.getName(), name)) {
            Optional<Account> accountByEmail = accountRepository.findAccountByEmail(email);
            if (accountByEmail.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            account.setName(email);
        }
    }


    public GeneralResponse getAccountByEmailAndPassword(String email, String password) {
        Optional<Account> accountByEmailAndPassword = accountRepository.findAccountByEmailAndPassword(email, password);
        if (!accountByEmailAndPassword.isPresent()) {
            return GeneralResponse.builder().message("faild").data("沒找到帳號").result(1).build();
        }
        var id = accountByEmailAndPassword.get().getId();

        return GeneralResponse.builder().message("susses").data("有找到帳號").result(0).build();
    }
}
