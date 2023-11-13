package com.example.eyeprotext.missionCompleteCount.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class FindTodayMissionCompleteResponse {
    private Integer ConcentrateTime;
    private List<UUID> missionId;
}
