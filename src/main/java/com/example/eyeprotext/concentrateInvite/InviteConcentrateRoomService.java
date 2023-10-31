package com.example.eyeprotext.concentrateInvite;

import com.example.eyeprotext.APNsPushy.APNsPushNotification;
import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.account.AccountRepository;
import com.example.eyeprotext.account.response.FindAccountResponse;
import com.example.eyeprotext.concentrateInvite.request.AddFriendToConcentrateRequest;
import com.example.eyeprotext.concentrateInvite.request.AddInviteRoomRequest;
import com.example.eyeprotext.concentrateInvite.request.RemoveInviteRoomRequest;
import com.example.eyeprotext.concentrateInvite.request.StartMutipleConcentrateRequest;
import com.example.eyeprotext.concentrateInvite.response.RefreshInviteRoomMemberListResponse;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecord;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InviteConcentrateRoomService {
    private final InviteConcentrateRoomRespository inviteConcentrateRoomRespository;
    private final AccountRepository accountRepository;
    private final ConcentrateRecordRepository concentrateRecordRepository;
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
            return GeneralResponse.builder().message("好友已經在你的房間").data("").result(0).build();
        }

        targetRoom.getReciveAccountId().add(RoomIdAndReciveAccountId.getReciveAccountId());

        Account reciveAccount = accountRepository.findById(RoomIdAndReciveAccountId.getReciveAccountId()).orElseThrow();
        Account sendAccount = accountRepository.findById(targetRoom.getSendAccountId()).orElseThrow();
        String deviceToken = reciveAccount.getDeviceToken();
        String msgBody = sendAccount.getName() + " 向你傳送專注邀請\n 房間 ID: " + RoomIdAndReciveAccountId.getInviteRoomId();
        APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);

        inviteConcentrateRoomRespository.save(targetRoom);
        return GeneralResponse.builder().message("已邀請").data("").result(0).build();
    }

//    public GeneralResponse anserInvite(AddInviteRequest anserInvite) {
//        Optional<Account> isExistReciveAccount = accountRepository.findById(anserInvite.getReciveAccountId());
//        if (!isExistReciveAccount.isPresent()) {
//            return GeneralResponse.builder().message("找不到邀請者的Id").data("").result(0).build();
//        }
//
//        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(anserInvite.getInviteRoomId());
//        if (!isExistInviteRoom.isPresent()) {
//            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
//        }
//
//        InviteConcentrateRoom inviteRoom = inviteConcentrateRoomRespository.findById(anserInvite.getInviteRoomId()).orElseThrow();
//        if (anserInvite.getAnser() == true) {
//            if (inviteRoom.getJoinAccountId().contains(anserInvite.getReciveAccountId())) {
//                return GeneralResponse.builder().message("已存在此房間").data("").result(0).build();
//            } else {
//
//                inviteRoom.getJoinAccountId().add(anserInvite.getReciveAccountId());
//                inviteConcentrateRoomRespository.save(inviteRoom);
//                Account sendAccount = accountRepository.findById(inviteRoom.getSendAccountId()).orElseThrow();
//                Account anserAccount = accountRepository.findById(anserInvite.getReciveAccountId()).orElseThrow();
//
//                String deviceToken = sendAccount.getDeviceToken();
//                String msgBody = anserAccount.getName() + "向你傳送專注邀請";
//                APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
//
//                for(int i = 0 ; i < inviteRoom.getJoinAccountId().size() ; i++) {
//                    Account joinAccount = accountRepository.findById(inviteRoom.getJoinAccountId().get(i)).orElseThrow(
//                            () -> new IllegalStateException("reciveId does not exists")
//                    );
//                    deviceToken = joinAccount.getDeviceToken();
//                    msgBody = anserAccount.getName() + "向你傳送專注邀請";
//                    APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
//                }
//                return GeneralResponse.builder().message("已加入此房間").data("").result(0).build();
//            }
//        } else {
//            return GeneralResponse.builder().message("已成功拒絕").data("").result(0).build();
//        }
//    }

    public GeneralResponse refreshInviteRoomMemberList(UUID inviteRoomId) {
        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(inviteRoomId);
        if (!isExistInviteRoom.isPresent()) {
            RefreshInviteRoomMemberListResponse refreshInviteRoomMemberListResponse = new RefreshInviteRoomMemberListResponse(new ArrayList<>());
            return GeneralResponse.builder().message("找不到房間的Id").data(refreshInviteRoomMemberListResponse).result(0).build();
        }

        InviteConcentrateRoom targetInviteRoom = inviteConcentrateRoomRespository.findById(inviteRoomId).orElseThrow();
        RefreshInviteRoomMemberListResponse refreshInviteRoomMemberListResponse = new RefreshInviteRoomMemberListResponse(new ArrayList<>());
        for(int i = 0 ; i < targetInviteRoom.getJoinAccountId().size() ; i++) {
            Account joinAccount = accountRepository.findById(targetInviteRoom.getJoinAccountId().get(i)).orElseThrow(
                    () -> new IllegalStateException("joinAccountId does not exists")
            );
            FindAccountResponse account = FindAccountResponse.builder()
                    .accountId(joinAccount.getAccountId())
                    .name(joinAccount.getName())
                    .image(joinAccount.getImage())
                    .build();
            refreshInviteRoomMemberListResponse.getMemberList().add(account);
        }

        return GeneralResponse.builder().message("成功更新此房間的成員").data(refreshInviteRoomMemberListResponse).result(0).build();
    }

    public GeneralResponse addToInviteRoom(AddInviteRoomRequest addInviteRoomRequest) {
        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(addInviteRoomRequest.getInviteRoomId());
        if (!isExistInviteRoom.isPresent()) {
            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
        }

        Optional<Account> isExistReciveAccount = accountRepository.findById(addInviteRoomRequest.getReciveAccountId());
        if (!isExistReciveAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到被邀請者的Id").data("").result(0).build();
        }

        Account reciveAccount = accountRepository.findById(addInviteRoomRequest.getReciveAccountId()).orElseThrow();
        InviteConcentrateRoom targetInviteRoom = inviteConcentrateRoomRespository.findById(addInviteRoomRequest.getInviteRoomId()).orElseThrow();
        targetInviteRoom.getJoinAccountId().add(addInviteRoomRequest.getReciveAccountId());
        inviteConcentrateRoomRespository.save(targetInviteRoom);
        return GeneralResponse.builder().message(reciveAccount.getName() + " 已加入房間").data("").result(0).build();

    }

    public GeneralResponse removeToInviteRoom(RemoveInviteRoomRequest removeInviteRoomRequest) {
        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(removeInviteRoomRequest.getInviteRoomId());
        if (!isExistInviteRoom.isPresent()) {
            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
        }

        Optional<Account> isExistRemoveAccount = accountRepository.findById(removeInviteRoomRequest.getRemoveAccountId());
        if (!isExistRemoveAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到被邀請者的Id").data("").result(0).build();
        }

        Account removeAccount = accountRepository.findById(removeInviteRoomRequest.getRemoveAccountId()).orElseThrow();
        InviteConcentrateRoom inviteConcentrateRoom = inviteConcentrateRoomRespository.findById(removeInviteRoomRequest.getInviteRoomId()).orElseThrow();
        inviteConcentrateRoom.getJoinAccountId().remove(removeAccount.getAccountId());
        inviteConcentrateRoomRespository.save(inviteConcentrateRoom);
        return GeneralResponse.builder().message(removeInviteRoomRequest.getRemoveAccountId() + "已離開房間").data("").result(0).build();
    }

    public GeneralResponse removeInviteRoom(InviteConcentrateRoom inviteConcentrateRoom) {
        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(inviteConcentrateRoom.getInviteRoomId());
        if (!isExistInviteRoom.isPresent()) {
            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
        }
        inviteConcentrateRoomRespository.deleteById(inviteConcentrateRoom.getInviteRoomId());

        return GeneralResponse.builder().message(inviteConcentrateRoom.getInviteRoomId().toString() + " 刪除成功").data("").result(0).build();
    }

    public GeneralResponse startMutipleConcentrate(StartMutipleConcentrateRequest startMutipleConcentrateRequest) {
        Optional<InviteConcentrateRoom> isExistInviteRoom = inviteConcentrateRoomRespository.findById(startMutipleConcentrateRequest.getInviteRoomId());
        if (!isExistInviteRoom.isPresent()) {
            return GeneralResponse.builder().message("找不到房間的Id").data("").result(0).build();
        }

        InviteConcentrateRoom targetInviteRoom = inviteConcentrateRoomRespository.findById(startMutipleConcentrateRequest.getInviteRoomId()).orElseThrow();
        for (int i = 0; i < targetInviteRoom.getJoinAccountId().size(); i++) {
            List<UUID> friendList = new ArrayList<>(targetInviteRoom.getJoinAccountId()); // 創建新的 friendList，複製 joinAccountId

            // 從 friendList 中刪除當前 accountId
            friendList.remove(targetInviteRoom.getJoinAccountId().get(i));

            ConcentrateRecord concentrateRecord = ConcentrateRecord.builder()
                    .concentrateTime(startMutipleConcentrateRequest.getConcentrateTime())
                    .inviteRoomId(startMutipleConcentrateRequest.getInviteRoomId())
                    .hostAccountId(targetInviteRoom.getSendAccountId())
                    .isFinished(false)
                    .startTime(startMutipleConcentrateRequest.getStartTime())
                    .accountId(targetInviteRoom.getJoinAccountId().get(i))
                    .restTime(startMutipleConcentrateRequest.getRestTime())
                    .withFriends(new ArrayList<>(friendList)) // 使用新的 friendList
                    .image("未上傳")
                    .description("")
                    .build();
            concentrateRecordRepository.save(concentrateRecord);
        }


        List<UUID> hostFriendList = new ArrayList<>();
        for (int i = 0;i <  targetInviteRoom.getJoinAccountId().size(); i++) {
            hostFriendList.add(targetInviteRoom.getJoinAccountId().get(i));
        }

        ConcentrateRecord hostConcentrateRecord = ConcentrateRecord.builder()
                .concentrateTime(startMutipleConcentrateRequest.getConcentrateTime())
                .inviteRoomId(startMutipleConcentrateRequest.getInviteRoomId())
                .hostAccountId(targetInviteRoom.getSendAccountId())
                .isFinished(false)
                .startTime(startMutipleConcentrateRequest.getStartTime())
                .accountId(targetInviteRoom.getSendAccountId())
                .restTime(startMutipleConcentrateRequest.getRestTime())
                .withFriends(hostFriendList)
                .image("未上傳")
                .description("")
                .build();
        concentrateRecordRepository.save(hostConcentrateRecord);

        return GeneralResponse.builder().message("進入專注模式成功").data("").result(0).build();
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
