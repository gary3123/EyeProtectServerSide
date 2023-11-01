package com.example.eyeprotext.news.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class LoadNewsResponse {
    private List<NewsItem> newsItems;
}
