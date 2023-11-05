package com.example.eyeprotext.concentrateRecord.response;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class SelfConcentrateRecordItem {
    private UUID recordId;
    private UUID accountId;
    private UUID hostAccountId;
    private String hostName;
    private String startTime;
    private String endTime;
    private Boolean isFinished;
    private String  concentrateTime;
    private String  restTime;
    @ElementCollection
    private List<String> withFriends;
}
