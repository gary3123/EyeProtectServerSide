package com.example.eyeprotext.concentrateRecord;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.account.AccountRepository;
import com.example.eyeprotext.account.FriendNameAndImage;
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
                .startTime(record.getStartTime())
                .concentrateTime(record.getConcentrateTime())
                .restTime(record.getRestTime())
                .isFinished(false)
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
}
