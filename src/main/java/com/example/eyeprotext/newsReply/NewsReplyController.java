package com.example.eyeprotext.newsReply;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.news.NewsService;
import com.example.eyeprotext.news.Request.AddNewsRequest;
import com.example.eyeprotext.newsReply.request.AddNewsReplyRequest;
import com.example.eyeprotext.newsReply.request.DeleteNewsReplyRequest;
import com.example.eyeprotext.newsReply.request.EditNewsReplyRequest;
import com.example.eyeprotext.newsReply.request.LoadNewsReplyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping(path = "api/newsReply")
public class NewsReplyController {
    private NewsReplyService newsReplyService;

    @Autowired
    public NewsReplyController(NewsReplyService newsReplyService) {
        this.newsReplyService = newsReplyService;
    }

    @PostMapping(path = "/addNewsReply")
    public GeneralResponse addNewsReply(@RequestBody AddNewsReplyRequest request) {
        return newsReplyService.addNewsReply(request);
    }

    @PostMapping(path = "/loadNewsReply")
    public GeneralResponse loadNewsReply(@RequestBody LoadNewsReplyRequest request) {
        return newsReplyService.loadNewsReply(request);
    }

    @PostMapping(path = "/deleteReply")
    public GeneralResponse deleteReply(@RequestBody DeleteNewsReplyRequest request) {
        return newsReplyService.deleteReply(request);
    }

    @PostMapping(path = "/editReply")
    public GeneralResponse editReply(@RequestBody EditNewsReplyRequest request) {
        return newsReplyService.editReply(request);
    }

}
