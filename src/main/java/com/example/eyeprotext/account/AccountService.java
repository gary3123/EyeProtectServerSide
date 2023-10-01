package com.example.eyeprotext.account;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.news.News;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void deleteAccount(UUID accountId) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException("account with id" + accountId + "does not exists");
        }
        accountRepository.deleteById(accountId);
    }
    @Transactional
    public void updateAccount(UUID accountId,
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

    @Transactional
    public GeneralResponse LoginAccount(String email, String password, String deviceToken) {
        Optional<Account> accountByEmailAndPassword = accountRepository.findAccountByEmailAndPassword(email, password);
        if (!accountByEmailAndPassword.isPresent()) {
            Account faildAccount = new Account(UUID.randomUUID(),
                    "",
                   "",
                    "",
                    "",
                    "",
                    "",
                    new ArrayList<>());
            return GeneralResponse.builder().message("faild").data(faildAccount).result(1).build();
        }
        var accountId = accountByEmailAndPassword.get().getAccountId();

        // 將找到的 id 放到 account 將實體帶進來
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalStateException("account with" +  accountId + "does not exists")
        );
        account.setDeviceToken(deviceToken);
        accountRepository.save(account);
        Hibernate.initialize(account.getFriendList());

        return GeneralResponse.builder().message("susses").data(account).result(0).build();
    }

    public GeneralResponse uploadImages(UUID accountId, String image) {
        // 將找到的 id 放到 account 將實體帶進來
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalStateException("account with" +  accountId + "does not exists")
        );
        account.setImage(image);
        accountRepository.save(account);
        return GeneralResponse.builder().message("susses").result(0).build();
    }

    @Transactional
    public GeneralResponse getAccountPersonInformation(UUID accountId) {
        // 將找到的 id 放到 account 將實體帶進來
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalStateException("account with" +  accountId + "does not exists")
        );
        Hibernate.initialize(account.getFriendList());
        return GeneralResponse.builder().message("susses").data(account).result(0).build();
    }

    @Transactional
    public GeneralResponse getFriendList(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalStateException("account with" +  accountId + "does not exists")
        );
        Hibernate.initialize(account.getFriendList());
        List<UUID> friendListUUID = account.getFriendList();
        List<FriendNameAndImage> friendNameAndImage = new ArrayList<>();
        for(int i = 0 ; i < friendListUUID.size() ; i++) {
            Account friendAccount = accountRepository.findById(friendListUUID.get(i)).orElseThrow(
                    () -> new IllegalStateException("account with" +  accountId + "does not exists")
            );
            Hibernate.initialize(friendAccount.getFriendList());
            FriendNameAndImage friendNameAndImageInfo = FriendNameAndImage.builder()
                    .accountId(friendAccount.getAccountId())
                    .name(friendAccount.getName())
                    .email(friendAccount.getEmail())
                    .image(friendAccount.getImage())
                    .build();

            friendNameAndImage.add(friendNameAndImageInfo);
        }
        return GeneralResponse.builder().message("susses").data(friendNameAndImage).result(0).build();
    }
}
