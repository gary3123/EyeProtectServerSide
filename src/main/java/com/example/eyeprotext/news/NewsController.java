package com.example.eyeprotext.news;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecord;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecordService;
import com.example.eyeprotext.news.Request.AddConcentrateToNewsRequest;
import com.example.eyeprotext.news.Request.AddNewsRequest;
import com.example.eyeprotext.news.Request.LoadNewsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/news")
public class NewsController {
    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping(path = "/addNews")
    public GeneralResponse addNews(@RequestBody AddNewsRequest request) {
        return newsService.addNews(request);
    }

    @PostMapping(path = "/addConcentrateToNews")
    public GeneralResponse addConcentrateToNews(@RequestBody AddConcentrateToNewsRequest request) {
        return newsService.addConcentrateToNews(request);
    }

    @PostMapping(path = "/loadNews")
    public GeneralResponse loadNews(@RequestBody LoadNewsRequest request) {
        return newsService.loadNews(request);
    }
}
