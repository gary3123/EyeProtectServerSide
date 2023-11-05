package com.example.eyeprotext.concentrateRecord.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FindConcentrateRecordByRecordIdResponse {
    private String picture;
    private String description;
}
