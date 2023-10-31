package com.example.eyeprotext.concentrateRecord.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class completeMutipleConcentrateRequest {
    private UUID accountId;
    private UUID inviteRoomId;
    private String endTime;
}
