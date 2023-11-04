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
public class EditNewsReplyRequest {
    private UUID replyId;
    private String message;
}