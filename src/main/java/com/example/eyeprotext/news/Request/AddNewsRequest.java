package com.example.eyeprotext.news.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AddNewsRequest {
    private UUID accountId;
    private String title;
    private String image;
    private String descripion;
    private String time;
}
