package com.example.eyeprotext.newsReply.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ReplyListResponse {
    private List<ReplyItem> replyList;
}
