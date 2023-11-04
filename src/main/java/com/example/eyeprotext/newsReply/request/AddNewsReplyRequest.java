package com.example.eyeprotext.newsReply.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddNewsReplyRequest {
    private UUID accountId;
    private UUID newsId;
    private String message;
    private String time;
}
