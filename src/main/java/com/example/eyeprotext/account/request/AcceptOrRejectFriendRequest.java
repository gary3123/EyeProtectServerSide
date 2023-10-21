package com.example.eyeprotext.account.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AcceptOrRejectFriendRequest {
    private UUID reciveAccountId;
    private UUID sendAccountId;
}
