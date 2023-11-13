package com.example.eyeprotext.missionCompleteCount.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AddMissionCompleteRequest {
    private UUID missionId;
    private UUID accountId;
    private String date;
}
