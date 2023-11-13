package com.example.eyeprotext.missionCompleteCount;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.missionCompleteCount.request.AddMissionCompleteRequest;
import com.example.eyeprotext.missionCompleteCount.request.FindTodayMissionCompleteRequest;
import com.example.eyeprotext.missionList.MissionListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/missionCompleteCount")
public class MissionCompleteCountController {
    private MissionCompleteCountService missionCompleteCountService;

    @Autowired
    public MissionCompleteCountController(MissionCompleteCountService missionCompleteCountService) {
        this.missionCompleteCountService = missionCompleteCountService;
    }

    @PostMapping(path = "/addMissionComplete")
    public GeneralResponse addMissionComplete(@RequestBody AddMissionCompleteRequest request) {
        return missionCompleteCountService.addMissionComplete(request);
    }

    @PostMapping(path = "/findTodayMissionComplete")
    public GeneralResponse findTodayMissionComplete(@RequestBody FindTodayMissionCompleteRequest request) {
        return missionCompleteCountService.findTodayMissionComplete(request);
    }
}
