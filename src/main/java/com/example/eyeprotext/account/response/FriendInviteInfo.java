package com.example.eyeprotext.account.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class FriendInviteInfo {
    private UUID accountId;
    private String name;
    private String email;
    private String image;
}
