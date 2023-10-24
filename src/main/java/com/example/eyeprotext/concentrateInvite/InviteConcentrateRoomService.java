package com.example.eyeprotext.concentrateInvite;

import com.example.eyeprotext.APNsPushy.APNsPushNotification;
import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.account.AccountRepository;
import com.example.eyeprotext.concentrateInvite.request.AddFriendToConcentrateRequest;
import com.example.eyeprotext.concentrateInvite.request.AnserInviteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InviteConcentrateRoomService {
    private final InviteConcentrateRoomRespository inviteConcentrateRoomRespository;
    private final AccountRepository accountRepository;
    private final Timer timer = new Timer();

    public List<InviteConcentrateRoom> getConcentrateInvite() {
        return inviteConcentrateRoomRespository.findAll();
    }

    public GeneralResponse createInviteRoom(InviteConcentrateRoom inviteConcentrateRoom) {

        Optional<Account> isExistSendAccount = accountRepository.findById(inviteConcentrateRoom.getSendAccountId());
        if (!isExistSendAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到寄出邀請者的Id").data("").result(0).build();
        }

        InviteConcentrateRoom newRoom = InviteConcentrateRoom.builder()
                .inviteRoomId(inviteConcentrateRoom.getInviteRoomId())
                .sendAccountId(inviteConcentrateRoom.getSendAccountId())
                .build();
        inviteConcentrateRoomRespository.save(newRoom);
        return GeneralResponse.builder().message("建立成功").data(newRoom.getInviteRoomId()).result(0).build();
    }

    public GeneralResponse addFriendToInviteRoom(AddFriendToConcentrateRequest RoomIdAndReciveAccountId) {
        Optional<Account> isExistReciveAccount = accountRepository.findById(RoomIdAndReciveAccountId.getReciveAccountId());
        if (!isExistReciveAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到被邀請者的Id").data("").result(0).build();
        }

        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(RoomIdAndReciveAccountId.getInviteRoomId());
        if (!isExistInviteRoom.isPresent()) {
            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
        }

        InviteConcentrateRoom targetRoom = inviteConcentrateRoomRespository.findById(RoomIdAndReciveAccountId.getInviteRoomId()).orElseThrow();
        if (targetRoom.getJoinAccountId().contains(RoomIdAndReciveAccountId.getReciveAccountId())) {
            return GeneralResponse.builder().message("好友已經在您的房間").data("").result(0).build();
        }

        targetRoom.getReciveAccountId().add(RoomIdAndReciveAccountId.getReciveAccountId());

        Account reciveAccount = accountRepository.findById(RoomIdAndReciveAccountId.getReciveAccountId()).orElseThrow();
        String deviceToken = reciveAccount.getDeviceToken();
        String msgBody = reciveAccount.getName() + "向你傳送專注邀請";
        APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
        inviteConcentrateRoomRespository.save(targetRoom);
        return GeneralResponse.builder().message("已邀請").data("").result(0).build();
    }

    public GeneralResponse anserInvite(AnserInviteRequest anserInvite) {
        Optional<Account> isExistReciveAccount = accountRepository.findById(anserInvite.getReciveAccountId());
        if (!isExistReciveAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到邀請者的Id").data("").result(0).build();
        }

        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(anserInvite.getInviteRoomId());
        if (!isExistInviteRoom.isPresent()) {
            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
        }

        InviteConcentrateRoom inviteRoom = inviteConcentrateRoomRespository.findById(anserInvite.getInviteRoomId()).orElseThrow();
        if (anserInvite.getAnser() == true) {
            if (inviteRoom.getJoinAccountId().contains(anserInvite.getReciveAccountId())) {
                return GeneralResponse.builder().message("已存在此房間").data("").result(0).build();
            } else {

                inviteRoom.getJoinAccountId().add(anserInvite.getReciveAccountId());
                inviteConcentrateRoomRespository.save(inviteRoom);
                Account sendAccount = accountRepository.findById(inviteRoom.getSendAccountId()).orElseThrow();
                Account anserAccount = accountRepository.findById(anserInvite.getReciveAccountId()).orElseThrow();

                String deviceToken = sendAccount.getDeviceToken();
                String msgBody = anserAccount.getName() + "向你傳送專注邀請";
                APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);

                for(int i = 0 ; i < inviteRoom.getJoinAccountId().size() ; i++) {
                    Account joinAccount = accountRepository.findById(inviteRoom.getJoinAccountId().get(i)).orElseThrow(
                            () -> new IllegalStateException("reciveId does not exists")
                    );
                    deviceToken = joinAccount.getDeviceToken();
                    msgBody = anserAccount.getName() + "向你傳送專注邀請";
                    APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
                }
                return GeneralResponse.builder().message("已加入此房間").data("").result(0).build();
            }
        } else {
            return GeneralResponse.builder().message("已成功拒絕").data("").result(0).build();
        }
    }

    public GeneralResponse refreshInviteRoomInfo(UUID inviteRoomId) {
        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(inviteRoomId);
        if (!isExistInviteRoom.isPresent()) {
            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
        }

        InviteConcentrateRoom targetInviteRoom = inviteConcentrateRoomRespository.findById(inviteRoomId).orElseThrow();
        return GeneralResponse.builder().message("成功搜尋到此房間").data(targetInviteRoom).result(0).build();
    }

//    public GeneralResponse refreshJoinAccount(ConcentrateInvite concentrateInvite) {
//        if (concentrateInvite.getJoinAccountId().isEmpty()) {
//            timer.cancel();
//        } else {
//            timer.schedule(refreshRoomJoinId(true),0,1000L);
//        }
//        return GeneralResponse.builder().message("打了").data("打了").result(0).build();
//    }
//
//    public TimerTask refreshRoomJoinId(boolean refresh) {
//
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println(refresh);
//            }
//        };
//        return task;
//    }


}
