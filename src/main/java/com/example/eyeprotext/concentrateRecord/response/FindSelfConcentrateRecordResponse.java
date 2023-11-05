package com.example.eyeprotext.concentrateRecord.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FindSelfConcentrateRecordResponse {
    private List<SelfConcentrateRecordItem> concentrateRecordList;
}
