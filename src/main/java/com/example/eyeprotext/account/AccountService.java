package com.example.eyeprotext.account;

import com.example.eyeprotext.APNsPushy.APNsPushNotification;
import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.request.AddFriendInviteRequest;
import com.example.eyeprotext.account.request.AcceptOrRejectFriendRequest;
import com.example.eyeprotext.account.response.FriendInviteInfo;
import com.example.eyeprotext.account.response.GetAccountPersonInformationResponse;
import com.example.eyeprotext.account.response.GetFriendInviteListResponse;
import com.example.eyeprotext.jwtConfig.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        targetAccount.setDeviceToken(account.getDeviceToken());
        accountRepository.save(targetAccount);
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
                    () -> new IllegalStateException("account with" + accountId + "does not exists")
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


    public GeneralResponse logout(UUID accountId) {
        Optional<Account> account =
                accountRepository.findById(accountId);
        if(!account.isPresent()) {
            return GeneralResponse.builder().message("susses").data("沒有找到此帳號").result(0).build();
        } else {
            Account targetAccount = accountRepository.findById(accountId).orElseThrow();
            targetAccount.setDeviceToken("");
            accountRepository.save(targetAccount);
            return GeneralResponse.builder().message("susses").data("登出成功").result(0).build();
        }
    }

    public GeneralResponse addFriendInvite(AddFriendInviteRequest request) {
        Optional<Account> account =
                accountRepository.findById(request.getAccountId());
        Optional<Account> invitedAccount =
                accountRepository.findAccountByNameAndEmail(request.getName(), request.getEmail());
        if(!account.isPresent() && !invitedAccount.isPresent()) {
            return GeneralResponse.builder().message("susses").data("沒有找到邀請者和被邀請者帳號").result(0).build();
        } else if (!account.isPresent()) {
            return GeneralResponse.builder().message("susses").data("沒有找到邀請者帳號").result(0).build();
        } else if (!invitedAccount.isPresent()) {
            return GeneralResponse.builder().message("susses").data("沒有找到被邀請者帳號").result(0).build();
        } else {
            Account targetAccount = accountRepository.findAccountByNameAndEmail(request.getName(), request.getEmail()).orElseThrow();
            Account sendInviteAccount = accountRepository.findById(request.getAccountId()).orElseThrow();
            if (targetAccount.getFriendList().contains(sendInviteAccount.getAccountId())) {
                return GeneralResponse.builder().message("susses").data("你和對方已經是好友").result(0).build();
            } else if (!targetAccount.getFriendInvites().contains(sendInviteAccount.getAccountId()) && !sendInviteAccount.getFriendInvites().contains(targetAccount.getAccountId())) {
                targetAccount.getFriendInvites().add(request.getAccountId());
                accountRepository.save(targetAccount);
            } else {
                if (sendInviteAccount.getFriendInvites().contains(targetAccount.getAccountId())) {
                    return GeneralResponse.builder().message("susses").data("對方已寄邀請給你").result(0).build();
                }
            }

            String deviceToken = targetAccount.getDeviceToken();
            String msgBody = sendInviteAccount.getName() + "傳送了好友邀請給你！";
            APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);

            return GeneralResponse.builder().message("susses").data("寄送邀請成功").result(0).build();
        }
    }

    public GeneralResponse getFriendInviteList(Account account) {
        Optional<Account> isExistAccount = accountRepository.findById(account.getAccountId());
        if (!isExistAccount.isPresent()) {
            GetFriendInviteListResponse response = new GetFriendInviteListResponse(new ArrayList<>());
            return GeneralResponse.builder().message("沒有此帳號ID").data(response).result(0).build();
        }

        Account targetAccount = accountRepository.findById(account.getAccountId()).orElseThrow();
        if (targetAccount.getFriendInvites().isEmpty()) {
            GetFriendInviteListResponse response = new GetFriendInviteListResponse(new ArrayList<>());
            return GeneralResponse.builder().message("此帳號目前沒有好友邀請").data(response).result(0).build();
        } else {
            GetFriendInviteListResponse response = new GetFriendInviteListResponse(new ArrayList<>());
            for (UUID friendInviteSenderID: targetAccount.getFriendInvites()) {
                Account inviteAccount = accountRepository.findById(friendInviteSenderID).orElseThrow();
                FriendInviteInfo friendInviteInfoItem =  new FriendInviteInfo(inviteAccount.getAccountId(), inviteAccount.getName(), inviteAccount.getEmail(), inviteAccount.getImage());
                response.friendinviteInfo.add(friendInviteInfoItem);
            }

            return GeneralResponse.builder().message("此帳號目前有好友邀請").data(response).result(0).build();
        }
    }

    public GeneralResponse addFriend(AcceptOrRejectFriendRequest request) {
        Optional<Account> isExistReciveAccount = accountRepository.findById(request.getReciveAccountId());
        Optional<Account> isExistAendAccount = accountRepository.findById(request.getSendAccountId());
        if (!isExistReciveAccount.isPresent()) {
            return GeneralResponse.builder().message("沒有找到寄出邀請的帳號ID").data("").result(0).build();
        }
        if (!isExistAendAccount.isPresent()) {
            return GeneralResponse.builder().message("沒有找到接受邀請的帳號ID").data("").result(0).build();
        }

        Account reciveAccount = accountRepository.findById(request.getReciveAccountId()).orElseThrow();
        Account sendAccount = accountRepository.findById(request.getSendAccountId()).orElseThrow();

        if(reciveAccount.getFriendInvites().contains(sendAccount)) {
            return GeneralResponse.builder().message("沒有在好友邀請中找到帳號ID").data("").result(0).build();
        }

        if(reciveAccount.getFriendList().contains(sendAccount.getAccountId())) {
            return GeneralResponse.builder().message("你和對方已經是好友了").data("").result(0).build();
        }

        reciveAccount.getFriendList().add(sendAccount.getAccountId());
        sendAccount.getFriendList().add(reciveAccount.getAccountId());
        reciveAccount.getFriendInvites().remove(sendAccount.getAccountId());
        accountRepository.save(sendAccount);
        accountRepository.save(reciveAccount);
        String deviceToken = sendAccount.getDeviceToken();
        String msgBody = reciveAccount.getName() + "和你成為朋友了！";
        APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
        return GeneralResponse.builder().message("新增好友成功").data("").result(0).build();
    }

    public GeneralResponse rejectFriendInvite(AcceptOrRejectFriendRequest request) {
        Optional<Account> isExistReciveAccount = accountRepository.findById(request.getReciveAccountId());
        Optional<Account> isExistAendAccount = accountRepository.findById(request.getSendAccountId());
        if (!isExistReciveAccount.isPresent()) {
            return GeneralResponse.builder().message("沒有找到寄出邀請的帳號ID").data("").result(0).build();
        }
        if (!isExistAendAccount.isPresent()) {
            return GeneralResponse.builder().message("沒有找到接受邀請的帳號ID").data("").result(0).build();
        }

        Account reciveAccount = accountRepository.findById(request.getReciveAccountId()).orElseThrow();
        Account sendAccount = accountRepository.findById(request.getSendAccountId()).orElseThrow();

        if(reciveAccount.getFriendInvites().contains(sendAccount)) {
            return GeneralResponse.builder().message("沒有在好友邀請中找到帳號ID").data("").result(0).build();
        }

        reciveAccount.getFriendInvites().remove(request.getSendAccountId());
        accountRepository.save(reciveAccount);
        return GeneralResponse.builder().message("拒絕邀請成功").data("").result(0).build();
    }
}
