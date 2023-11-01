package com.example.eyeprotext.concentrateRecord.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UseInviteRoomIdAndAccountIdTofindConcentrateRecordIdRequest {
    private UUID inviteRoomId;
    private UUID accountId;
}

