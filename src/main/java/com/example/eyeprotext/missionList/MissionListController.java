package com.example.eyeprotext.missionList;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/missionList")
public class MissionListController {
    private MissionListService missionListService;

    @Autowired
    public MissionListController(MissionListService missionListService) {
        this.missionListService = missionListService;
    }

    @GetMapping(path = "/getAll")
    public GeneralResponse fetchMissionList() {
        return missionListService.fetchMission();
    }
}

