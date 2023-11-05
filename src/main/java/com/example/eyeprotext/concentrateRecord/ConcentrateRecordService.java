package com.example.eyeprotext.concentrateRecord;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.account.AccountRepository;
import com.example.eyeprotext.account.FriendNameAndImage;
import com.example.eyeprotext.concentrateRecord.request.*;
import com.example.eyeprotext.concentrateRecord.response.FindByInviteRoomIdForConcentrateAndRestTimeResponse;
import com.example.eyeprotext.concentrateRecord.response.FindConcentrateRecordByRecordIdResponse;
import com.example.eyeprotext.concentrateRecord.response.FindSelfConcentrateRecordResponse;
import com.example.eyeprotext.concentrateRecord.response.SelfConcentrateRecordItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConcentrateRecordService {
    private final ConcentrateRecordRepository concentrateRecordRepository;
    private final AccountRepository accountRepository;

    public List<ConcentrateRecord> getConcentrateRecord() {
        return concentrateRecordRepository.findAll();
    }

    public GeneralResponse addConcentrateRecord(ConcentrateRecord record) {

        Optional<Account> isExistHostAccountId = accountRepository.findById(record.getHostAccountId());
        if (!isExistHostAccountId.isPresent()) {
            return GeneralResponse.builder().message("找不到主持專注模式的 AccountId").data("").result(0).build();
        }

        Optional<Account> isExistAccountId = accountRepository.findById(record.getAccountId());
        if (!isExistAccountId.isPresent()) {
            return GeneralResponse.builder().message("找不到執行專注模式的 AccountId").data("").result(0).build();
        }

        List<UUID> withfriendListUUID = record.getWithFriends();
        List<FriendNameAndImage> friendNameAndImage = new ArrayList<>();
        for(int i = 0 ; i < withfriendListUUID.size() ; i++) {
            Optional<Account> friendId = accountRepository.findById(record.getAccountId());
            if (!friendId.isPresent()) {
                return GeneralResponse.builder().message("好友 " + withfriendListUUID.get(i)).data("").result(0).build();
            }
        }

        ConcentrateRecord addConcentrateRecord = ConcentrateRecord.builder()
                .accountId(record.getAccountId())
                .hostAccountId(record.getHostAccountId())
                .startTime(record.getStartTime())
                .concentrateTime(record.getConcentrateTime())
                .restTime(record.getRestTime())
                .isFinished(false)
                .image("未上傳")
                .description("")
                .build();
        concentrateRecordRepository.save(addConcentrateRecord);

        return GeneralResponse.builder().message("已新增紀錄").data(addConcentrateRecord.getRecordId()).result(0).build();
    }

    public GeneralResponse giveUpConcentrateRecord(ConcentrateRecord record) {
        Optional<ConcentrateRecord> isExistedRecord = concentrateRecordRepository.findById(record.getRecordId());
        if (!isExistedRecord.isPresent()) {
            return GeneralResponse.builder().message("沒有此紀錄").data("").result(0).build();
        }

        ConcentrateRecord targetRecord = concentrateRecordRepository.findById(record.getRecordId()).orElseThrow();
        targetRecord.setIsFinished(false);
        targetRecord.setEndTime(record.getEndTime());
        concentrateRecordRepository.save(targetRecord);
        return GeneralResponse.builder().message("已更新紀錄").data("").result(0).build();
    }

    public GeneralResponse completeConcentrateRecord(ConcentrateRecord record) {
        Optional<ConcentrateRecord> isExistedRecord = concentrateRecordRepository.findById(record.getRecordId());
        if (!isExistedRecord.isPresent()) {
            return GeneralResponse.builder().message("沒有此紀錄").data("").result(0).build();
        }

        ConcentrateRecord targetRecord = concentrateRecordRepository.findById(record.getRecordId()).orElseThrow();
        targetRecord.setIsFinished(true);
        targetRecord.setEndTime(record.getEndTime());
        concentrateRecordRepository.save(targetRecord);
        return GeneralResponse.builder().message("已更新紀錄").data("").result(0).build();
    }

    public GeneralResponse findByInviteRoomIdForConcentrateAndRestTime(ConcentrateRecord record) {
        List<ConcentrateRecord> isExistedRecords = concentrateRecordRepository.findConcentrateRecordByInviteRoomId(record.getInviteRoomId());
        if (isExistedRecords.isEmpty()) {
            FindByInviteRoomIdForConcentrateAndRestTimeResponse response = FindByInviteRoomIdForConcentrateAndRestTimeResponse.builder().build();
            return GeneralResponse.builder().message("未找到 inviteRoomId 相關的紀錄").data(response).result(0).build();
        }
        ConcentrateRecord targetConcentrateRecord = isExistedRecords.get(0);

        FindByInviteRoomIdForConcentrateAndRestTimeResponse response = FindByInviteRoomIdForConcentrateAndRestTimeResponse.builder().concentrateTime(targetConcentrateRecord.getConcentrateTime()).restTime(targetConcentrateRecord.getRestTime()).build();
        return GeneralResponse.builder().message("找到相關紀錄了").data(response).result(0).build();
    }

    public GeneralResponse completeMutipleConcentrate(completeMutipleConcentrateRequest request) {
        List<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findConcentrateRecordByInviteRoomId(request.getInviteRoomId());
        if (isExistConcentrateRecord.isEmpty()) {
            return GeneralResponse.builder().message("未找到 inviteRoomId 的相關紀錄").data("").result(0).build();
        }

        for (int i = 0;i < isExistConcentrateRecord.size(); i++) {
            ConcentrateRecord concentrateRecord = isExistConcentrateRecord.get(i);
            concentrateRecord.setIsFinished(true);
            concentrateRecord.setEndTime(request.getEndTime());
            concentrateRecordRepository.save(concentrateRecord);
        }
        return GeneralResponse.builder().message("已更新成功").data("").result(0).build();
    }

    public GeneralResponse uploadAlongRecordImage(UploadAlongRecordImageRequest request) {
        Optional<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findById(request.getRecordId());
        if (!isExistConcentrateRecord.isPresent()) {
            return GeneralResponse.builder().message("沒有找到對應的 recordId").data("").result(0).build();
        }
        ConcentrateRecord concentrateRecord = isExistConcentrateRecord.get();
        concentrateRecord.setImage(request.getImage());
        concentrateRecord.setDescription(request.getDescription());
        concentrateRecordRepository.save(concentrateRecord);
        return GeneralResponse.builder().message("更新成功").data(concentrateRecord.getRecordId()).result(0).build();
    }

    public GeneralResponse uploadMtipleRecordImage(UploadMtipleRecordImageRequest request) {
        Optional<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findConcentrateRecordByInviteRoomIdAndAccountId(request.getInviteRoomId(), request.getAccountId());
        if (!isExistConcentrateRecord.isPresent()) {
            return GeneralResponse.builder().message("沒有找到對應的 inviteRoomId 和 accountId").data("").result(0).build();
        }
        ConcentrateRecord concentrateRecord = isExistConcentrateRecord.get();
        concentrateRecord.setImage(request.getImage());
        concentrateRecord.setDescription(request.getDescription());
        concentrateRecordRepository.save(concentrateRecord);
        return GeneralResponse.builder().message("更新成功").data(concentrateRecord.getRecordId()).result(0).build();
    }

    public GeneralResponse useInviteRoomIdAndAccountIdTofindConcentrateRecordId(UseInviteRoomIdAndAccountIdTofindConcentrateRecordIdRequest request) {
        Optional<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findConcentrateRecordByInviteRoomIdAndAccountId(request.getInviteRoomId(), request.getAccountId());
        if (!isExistConcentrateRecord.isPresent()) {
            return GeneralResponse.builder().message("沒有找到對應的 inviteRoomId 和 accountId").data("").result(0).build();
        }

        ConcentrateRecord concentrateRecord = isExistConcentrateRecord.get();
        return GeneralResponse.builder().message("查詢成功").data(concentrateRecord.getRecordId()).result(0).build();
    }

    public GeneralResponse findSelfConcentrateRecord(FindSelfConcentrateRecordRequest request) {
        Optional<Account> isExistAccountId = accountRepository.findById(request.getAccountId());
        FindSelfConcentrateRecordResponse targetResponse = FindSelfConcentrateRecordResponse.builder().concentrateRecordList(new ArrayList<SelfConcentrateRecordItem>()).build();

        if (!isExistAccountId.isPresent()) {
            return GeneralResponse.builder().message("找不到 AccountId").data(targetResponse).result(0).build();
        }

        List<ConcentrateRecord> isExistConcentrateRecordList = concentrateRecordRepository.findConcentrateRecordByAccountId(isExistAccountId.get().getAccountId());
        if (isExistConcentrateRecordList.isEmpty()) {
            return GeneralResponse.builder().message("ConcentrateRecord 是空的").data(targetResponse).result(0).build();
        }

        for(int i = 0;i < isExistConcentrateRecordList.size(); i++) {
            List<String> friensNameList = new ArrayList<String>();
            for(int a = 0;a < isExistConcentrateRecordList.get(i).getWithFriends().size(); a++ ) {
                Optional<Account> isExistFriendAccountId = accountRepository.findById(isExistConcentrateRecordList.get(i).getWithFriends().get(a));
                if (!isExistFriendAccountId.isPresent()) {
                    return GeneralResponse.builder().message("找不到 FriendAccountId").data(targetResponse).result(0).build();
                }
                friensNameList.add(isExistFriendAccountId.get().getName());
            }

            Optional<Account> isExistHostAccountId = accountRepository.findById(isExistConcentrateRecordList.get(i).getHostAccountId());
            if (!isExistHostAccountId.isPresent()) {
                return GeneralResponse.builder().message("找不到 HostAccountId").data(targetResponse).result(0).build();
            }
                SelfConcentrateRecordItem targetConcentrateRecordItem = SelfConcentrateRecordItem.builder()
                        .recordId(isExistConcentrateRecordList.get(i).getRecordId())
                        .endTime(isExistConcentrateRecordList.get(i).getEndTime())
                        .withFriends(friensNameList)
                        .concentrateTime(isExistConcentrateRecordList.get(i).getConcentrateTime())
                        .hostAccountId(isExistConcentrateRecordList.get(i).getAccountId())
                        .hostName(isExistHostAccountId.get().getName())
                        .isFinished(isExistConcentrateRecordList.get(i).getIsFinished())
                        .startTime(isExistConcentrateRecordList.get(i).getStartTime())
                        .accountId(isExistConcentrateRecordList.get(i).getAccountId())
                        .restTime(isExistConcentrateRecordList.get(i).getRestTime())
                        .build();
                targetResponse.getConcentrateRecordList().add(targetConcentrateRecordItem);
        }
        return GeneralResponse.builder().message("找到 ConcentrateRecord 了").data(targetResponse).result(0).build();
    }

    public GeneralResponse findConcentrateRecordByRecordId(ConcentrateRecord record) {
        Optional<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findById(record.getRecordId());
        if (isExistConcentrateRecord.isEmpty()) {
            return GeneralResponse.builder().message("沒有此紀錄").data("").result(0).build();
        }
        ConcentrateRecord tergetConcentrate = isExistConcentrateRecord.get();

        FindConcentrateRecordByRecordIdResponse targetResponse = FindConcentrateRecordByRecordIdResponse.builder()
                .picture(tergetConcentrate.getImage())
                .description(tergetConcentrate.getDescription())
                .build();
        return GeneralResponse.builder().message("找到 ConcentrateRecord 了").data(targetResponse).result(0).build();
    }
}
