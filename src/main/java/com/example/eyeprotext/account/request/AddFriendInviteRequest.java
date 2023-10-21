package com.example.eyeprotext.account.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AddFriendInviteRequest {
    private UUID accountId;
    private String name;
    private  String email;
}
