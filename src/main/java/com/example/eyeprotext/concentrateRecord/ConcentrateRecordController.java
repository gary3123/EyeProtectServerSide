package com.example.eyeprotext.concentrateRecord;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.concentrateRecord.request.*;
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

    @PostMapping(path = "/findByInviteRoomIdForConcentrateAndRestTime")
    public GeneralResponse findByInviteRoomIdForConcentrateAndRestTime(@RequestBody ConcentrateRecord record) {
        return concentrateRecordService.findByInviteRoomIdForConcentrateAndRestTime(record);
    }

    @PostMapping(path = "/completeMutipleConcentrate")
    public GeneralResponse completeMutipleConcentrate(@RequestBody completeMutipleConcentrateRequest request) {
        return concentrateRecordService.completeMutipleConcentrate(request);
    }

    @PostMapping(path = "/uploadAlongRecordImage")
    public GeneralResponse uploadAlongRecordImage(@RequestBody UploadAlongRecordImageRequest request) {
        return concentrateRecordService.uploadAlongRecordImage(request);
    }

    @PostMapping(path = "/uploadMtipleRecordImage")
    public GeneralResponse uploadMtipleRecordImage(@RequestBody UploadMtipleRecordImageRequest request) {
        return concentrateRecordService.uploadMtipleRecordImage(request);
    }

    @PostMapping(path = "/useInviteRoomIdAndAccountIdTofindConcentrateRecordId")
    public GeneralResponse useInviteRoomIdAndAccountIdTofindConcentrateRecordId(@RequestBody UseInviteRoomIdAndAccountIdTofindConcentrateRecordIdRequest request) {
        return concentrateRecordService.useInviteRoomIdAndAccountIdTofindConcentrateRecordId(request);
    }

    @PostMapping(path = "/findSelfConcentrateRecord")
    public GeneralResponse findSelfConcentrateRecord(@RequestBody FindSelfConcentrateRecordRequest request) {
        return concentrateRecordService.findSelfConcentrateRecord(request);
    }

    @PostMapping(path = "/findConcentrateRecordByRecordId")
    public GeneralResponse findConcentrateRecordByRecordId(@RequestBody ConcentrateRecord record) {
        return concentrateRecordService.findConcentrateRecordByRecordId(record);
    }
}
