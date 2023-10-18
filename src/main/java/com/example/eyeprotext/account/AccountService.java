package com.example.eyeprotext.account;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.response.GetAccountPersonInformationResponse;
import com.example.eyeprotext.config.JwtService;
import com.example.eyeprotext.missionList.MissionListRepository;
import com.example.eyeprotext.news.News;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.eyeprotext.config.ApplicationConfig;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public List<Account> getAccount() {
        return accountRepository.findAll();
    }

    public GeneralResponse addNewAccount(Account account) {
        Optional<Account> accountByEmail =
                accountRepository.findAccountByEmail(account.getEmail());
        if (accountByEmail.isPresent()) {
            return GeneralResponse.builder().message("faild").data("註冊到重複的帳號密碼").result(1).build();
        }
        var newAccount = Account.builder()
                .accountId(account.getAccountId())
                .dor(account.getDor())
                .email(account.getEmail())
                .password(passwordEncoder.encode(account.getPassword()))
                .role(Role.User)
                .name(account.getName())
                .image("未設置")
                .build();
        accountRepository.save(newAccount);
        return GeneralResponse.builder().message("susses").data("註冊成功").result(0).build();
    }

    public AuthenticationResponse loginAccount(Account account) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        account.getEmail(),
                        account.getPassword()
                )
        );
        var targetAccount = accountRepository.findAccountByEmail(account.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder().data(targetAccount).token(jwtToken).message("登入成功").build();
    }

//    @Transactional
//    public GeneralResponse LoginAccount(String email, String password, String deviceToken) {
//        Optional<Account> accountByEmailAndPassword = accountRepository.findAccountByEmailAndPassword(email, password);
//        if (!accountByEmailAndPassword.isPresent()) {
//            Account faildAccount = new Account(UUID.randomUUID(),
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    new ArrayList<>(),
//                    Role.User);
//            return GeneralResponse.builder().message("faild").data(faildAccount).result(1).build();
//        }
//        var accountId = accountByEmailAndPassword.get().getAccountId();
//
//        // 將找到的 id 放到 account 將實體帶進來
//        Account account = accountRepository.findById(accountId).orElseThrow(
//                () -> new IllegalStateException("account with" +  accountId + "does not exists")
//        );
//        account.setDeviceToken(deviceToken);
//        accountRepository.save(account);
//        Hibernate.initialize(account.getFriendList());
//
//        return GeneralResponse.builder().message("susses").data(account).result(0).build();
//    }

//    public GeneralResponse addNewAccount(Account account) {
//        Optional<Account> accountByEmail =
//                accountRepository.findAccountByEmail(account.getEmail());
//        if (accountByEmail.isPresent()) {
//            return GeneralResponse.builder().message("faild").data("註冊到重複的帳號密碼").result(1).build();
//        }
//        accountRepository.save(account);
//        return GeneralResponse.builder().message("susses").data("註冊成功").result(0).build();
//    }

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

    public GeneralResponse uploadImages(UUID accountId, String image) {
        // 將找到的 id 放到 account 將實體帶進來
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalStateException("account with" +  accountId + "does not exists")
        );
        account.setImage(image);
        accountRepository.save(account);
        return GeneralResponse.builder().message("susses").result(0).build();
    }

    public GeneralResponse getAccountPersonInformation(UUID accountId) {
        // 將找到的 id 放到 account 將實體帶進來
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalStateException("account with" +  accountId + "does not exists")
        );
        GetAccountPersonInformationResponse response = new GetAccountPersonInformationResponse(account.getAccountId(),
                account.getEmail(),
                account.getName(),
                account.getDor(),
                account.getImage());
        return GeneralResponse.builder().message("susses").data(response).result(0).build();
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
