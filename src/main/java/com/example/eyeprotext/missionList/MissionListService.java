package com.example.eyeprotext.missionList;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionListService {
    private final MissionListRepository missionListRepository;

    @Autowired
    public MissionListService(MissionListRepository missionListRepository) {
        this.missionListRepository = missionListRepository;
    }


    public GeneralResponse fetchMission() {
        List<MissionList> missionListLists = missionListRepository.findAll();
        return GeneralResponse.builder().message("susses").data(missionListLists).result(0).build();
    }
}
