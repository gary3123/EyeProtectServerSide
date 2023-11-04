package com.example.eyeprotext.newsReply.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ReplyItem {
    private UUID replyId;
    private UUID newsId;
    private UUID accountId;
    private String accountName;
    private String accountImage;
    private String message;
    private String time;
}
