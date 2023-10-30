package com.example.eyeprotext.concentrateRecord.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindByInviteRoomIdForConcentrateAndRestTimeResponse {
    private String concentrateTime;
    private String restTime;
}
