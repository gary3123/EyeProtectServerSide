package com.example.eyeprotext.missionCompleteCount;

import com.example.eyeprotext.APNsPushy.APNsPushNotification;
import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.account.AccountRepository;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecord;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecordRepository;
import com.example.eyeprotext.missionCompleteCount.request.AddMissionCompleteRequest;
import com.example.eyeprotext.missionCompleteCount.request.FindTodayMissionCompleteRequest;
import com.example.eyeprotext.missionCompleteCount.response.FindTodayMissionCompleteResponse;
import com.example.eyeprotext.missionList.MissionList;
import com.example.eyeprotext.missionList.MissionListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionCompleteCountService {
    private final AccountRepository accountRepository;
    private final MissionCompleteCountRepository missionCompleteCountRepository;
    private final ConcentrateRecordRepository concentrateRecordRepository;
    private final MissionListRepository missionListRepository;

    public GeneralResponse addMissionComplete(AddMissionCompleteRequest request) {

        Optional<MissionList> isExistMission = missionListRepository.findById(request.getMissionId());

        if(isExistMission.isEmpty()) {
            return GeneralResponse.builder().message("沒有此任務").data("").result(0).build();
        }

        String todayDate = request.getDate().substring(0,10);

        List<MissionCompleteCount> isExistMissionIdAndDate = missionCompleteCountRepository.findMissionCompleteCountByAccountIdAndMissionIdAndDate(request.getAccountId(), request.getMissionId(), todayDate);

        System.out.println(request.getAccountId() + "," + request.getMissionId() + "," + todayDate);
        System.out.println("isExistMissionIdAndDate" + isExistMissionIdAndDate.isEmpty());

        if(!isExistMissionIdAndDate.isEmpty()) {
            return GeneralResponse.builder().message("本日已紀錄過").data("").result(0).build();
        }

        var targetMissionCompleteCount = MissionCompleteCount.builder()
                .missionID(isExistMission.get().getMissionID())
                .accountId(request.getAccountId())
                .title(isExistMission.get().getTitle())
                .date(request.getDate())
                .build();

        missionCompleteCountRepository.save(targetMissionCompleteCount);

        String completeMissionDate = request.getDate().substring(0,10);
        List<MissionCompleteCount> isCompleteTodayMission = missionCompleteCountRepository.findMissionCompleteCountByAccountIdAndDate(request.getAccountId(), completeMissionDate);
        Integer todayMissionCompleteCount = isCompleteTodayMission.size();
        System.out.println("isCompleteTodayMission.size() = " + isCompleteTodayMission.size());



        List<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findConcentrateRecordByAccountIdAndDate(request.getAccountId(), request.getDate());

        List<MissionList> isExistMissionList = missionListRepository.findMissionListByTitle("使用專注模式");

        var concentrateTimeCount = 0;
        for(int i = 0; i < isExistConcentrateRecord.size() ;i++) {
            if (Integer.parseInt(isExistConcentrateRecord.get(i).getConcentrateTime().substring(0,1)) != 0) {
                concentrateTimeCount += Integer.parseInt(isExistConcentrateRecord.get(i).getConcentrateTime().substring(0,1)) * 60;
            }
            concentrateTimeCount += Integer.parseInt(isExistConcentrateRecord.get(i).getConcentrateTime().substring(3));
        }

        for(int i = 0; i < isExistMissionList.size(); i++) {
            if(concentrateTimeCount >= isExistMissionList.get(i).getProgress()) {
                todayMissionCompleteCount += 1;
            }
        }
        if (todayMissionCompleteCount >= 5) {
            pushNotificationForCompleteMission(request.getAccountId());
        }

        return GeneralResponse.builder().message("已儲存紀錄").data("").result(0).build();
    }

    public void pushNotificationForCompleteMission(UUID accountId) {
        Optional<Account> isExistAccountId = accountRepository.findById(accountId);

        for (int i = 0; i < isExistAccountId.get().getFriendList().size(); i++) {
            Optional<Account> isExistFriend = accountRepository.findById(isExistAccountId.get().getFriendList().get(i));
            String deviceToken = isExistFriend.get().getDeviceToken();
            String msgBody = isExistAccountId.get().getName() + " 完成了每日任務，趕緊跟上腳步一起護眼！";
            APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
        }
        String deviceToken = isExistAccountId.get().getDeviceToken();
        String msgBody = " 您已完成了每日任務！";
        APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
    }

    public GeneralResponse findTodayMissionComplete(FindTodayMissionCompleteRequest request) {
        List<MissionCompleteCount> isExistMissionCompleteCount = missionCompleteCountRepository.findMissionCompleteCountByAccountIdAndDate(request.getAccountId(), request.getDate());

        List<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findConcentrateRecordByAccountIdAndDate(request.getAccountId(), request.getDate());

        List<MissionList> isExistMissionList = missionListRepository.findMissionListByTitle("使用專注模式");

        List<UUID> completeMissionId = new ArrayList<UUID>();

        var concentrateTimeCount = 0;
        for(int i = 0; i < isExistConcentrateRecord.size() ;i++) {
            if (Integer.parseInt(isExistConcentrateRecord.get(i).getConcentrateTime().substring(0,1)) != 0) {
                concentrateTimeCount += Integer.parseInt(isExistConcentrateRecord.get(i).getConcentrateTime().substring(0,1)) * 60;
            }
            concentrateTimeCount += Integer.parseInt(isExistConcentrateRecord.get(i).getConcentrateTime().substring(3));
        }

        for(int i = 0; i < isExistMissionList.size(); i++) {
            if(concentrateTimeCount >= isExistMissionList.get(i).getProgress()) {
                completeMissionId.add(isExistMissionList.get(i).getMissionID());
            }
        }

        if(isExistMissionCompleteCount.isEmpty()) {
            return GeneralResponse.builder().message("已取得完成任務清單").data(FindTodayMissionCompleteResponse.builder().ConcentrateTime(concentrateTimeCount).missionId(completeMissionId).build()).result(0).build();
        }

        for(int i = 0; i < isExistMissionCompleteCount.size(); i++) {
            completeMissionId.add(isExistMissionCompleteCount.get(i).getMissionID());
        }

        return GeneralResponse.builder().message("已取得完成任務清單").data(FindTodayMissionCompleteResponse.builder().ConcentrateTime(concentrateTimeCount).missionId(completeMissionId).build()).result(0).build();
    }
}
