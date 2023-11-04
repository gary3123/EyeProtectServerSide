package com.example.eyeprotext.news.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class NewsItem {
    private UUID newsId;
    private UUID sendAccountId;
    private String sendAccountName;
    private String sendAccountImage;
    private String title;
    private String description;
    private String NewsPicture;
    private String time;
    private Integer replyCount;
}
