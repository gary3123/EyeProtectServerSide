package com.example.eyeprotext.account.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetFriendInviteListResponse {
    public List<FriendInviteInfo> friendinviteInfo;
}
