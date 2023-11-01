package com.example.eyeprotext.news.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class LoadNewsRequest {
    private UUID accountId;
}
