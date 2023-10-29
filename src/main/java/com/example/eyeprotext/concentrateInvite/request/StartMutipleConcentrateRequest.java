package com.example.eyeprotext.concentrateInvite.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class StartMutipleConcentrateRequest {
    private UUID inviteRoomId;
    private String startTime;
    private String  concentrateTime;
    private String  restTime;
}
