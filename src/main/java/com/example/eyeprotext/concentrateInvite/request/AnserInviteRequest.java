package com.example.eyeprotext.concentrateInvite.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AnserInviteRequest {
    private UUID inviteRoomId;
    private UUID reciveAccountId;
    private Boolean anser;

}
