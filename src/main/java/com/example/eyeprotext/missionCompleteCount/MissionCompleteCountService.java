package com.example.eyeprotext.missionCompleteCount;

import com.example.eyeprotext.GeneralResponse;
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
    private final MissionCompleteCountRepository missionCompleteCountRepository;
    private final ConcentrateRecordRepository concentrateRecordRepository;
    private final MissionListRepository missionListRepository;

    public GeneralResponse addMissionComplete(AddMissionCompleteRequest request) {

        Optional<MissionList> isExistMission = missionListRepository.findById(request.getMissionId());

        if(isExistMission.isEmpty()) {
            return GeneralResponse.builder().message("沒有此任務").data("").result(0).build();
        }

        Optional<MissionCompleteCount> isExistMissionIdAndDate = missionCompleteCountRepository.findMissionCompleteCountByAccountIdAndMissionIdAndDate(request.getAccountId(), request.getMissionId(), request.getDate());

        if(isExistMissionIdAndDate.isPresent()) {
            return GeneralResponse.builder().message("本日已紀錄過").data("").result(0).build();
        }

        var targetMissionCompleteCount = MissionCompleteCount.builder()
                .missionID(isExistMission.get().getMissionID())
                .accountId(request.getAccountId())
                .title(isExistMission.get().getTitle())
                .date(request.getDate())
                .build();

        missionCompleteCountRepository.save(targetMissionCompleteCount);
        return GeneralResponse.builder().message("已儲存紀錄").data("").result(0).build();
    }

    public GeneralResponse findTodayMissionComplete(FindTodayMissionCompleteRequest request) {
        List<MissionCompleteCount> isExistMissionCompleteCount = missionCompleteCountRepository.findMissionCompleteCountByAccountIdAndDate(request.getAccountId(), request.getDate());

        List<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findConcentrateRecordByAccountIdAndDate(request.getAccountId(), request.getDate());

        List<MissionList> isExistMissionList = missionListRepository.findMissionListByTitle("使用專注模式");

        List<UUID> completeMissionId = new ArrayList<UUID>();

        var concentrateTimeCount = 0;
        for(int i = 0; i < isExistConcentrateRecord.size() ;i++) {
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
