package com.example.eyeprotext.concentrateRecord;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/concentrateRecord")
public class ConcentrateRecordController {
    private ConcentrateRecordService concentrateRecordService;

    @Autowired
    public ConcentrateRecordController(ConcentrateRecordService concentrateRecordService) {
        this.concentrateRecordService = concentrateRecordService;
    }

    @PostMapping(path = "/addConcentrateRecord")
    public GeneralResponse addConcentrateRecord(@RequestBody ConcentrateRecord record) {
        return concentrateRecordService.addConcentrateRecord(record);
    }

    @PostMapping(path = "/giveUpConcentrateRecord")
    public GeneralResponse giveUpConcentrateRecord(@RequestBody ConcentrateRecord record) {
        return concentrateRecordService.giveUpConcentrateRecord(record);
    }

    @PostMapping(path = "/completeConcentrateRecord")
    public GeneralResponse completeConcentrateRecord(@RequestBody ConcentrateRecord record) {
        return concentrateRecordService.completeConcentrateRecord(record);
    }

}
