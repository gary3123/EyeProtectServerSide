package com.example.eyeprotext.concentrateInvite.response;

import com.example.eyeprotext.account.response.FindAccountResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RefreshInviteRoomMemberListResponse {
    private List<FindAccountResponse> memberList;
}
