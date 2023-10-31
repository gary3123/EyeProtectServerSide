package com.example.eyeprotext.concentrateRecord.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UploadAlongRecordImageRequest {
    private UUID recordId;
    private String image;
    private String description;
}
