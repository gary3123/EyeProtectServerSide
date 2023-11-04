package com.example.eyeprotext.newsReply;

import com.example.eyeprotext.APNsPushy.APNsPushNotification;
import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.account.AccountRepository;
import com.example.eyeprotext.news.News;
import com.example.eyeprotext.news.NewsRepository;
import com.example.eyeprotext.news.Request.AddNewsRequest;
import com.example.eyeprotext.newsReply.request.AddNewsReplyRequest;
import com.example.eyeprotext.newsReply.request.DeleteNewsReplyRequest;
import com.example.eyeprotext.newsReply.request.EditNewsReplyRequest;
import com.example.eyeprotext.newsReply.request.LoadNewsReplyRequest;
import com.example.eyeprotext.newsReply.response.ReplyItem;
import com.example.eyeprotext.newsReply.response.ReplyListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsReplyService {
    private final NewsRepository newsRepository;
    private final AccountRepository accountRepository;
    private final NewsReplyRepository newsReplyRepository;

    public GeneralResponse addNewsReply(AddNewsReplyRequest request) {
        Optional<Account> isExistSendAccount = accountRepository.findById(request.getAccountId());
        if (isExistSendAccount.isEmpty()) {
            return GeneralResponse.builder().message("沒有找到留言的帳號 ID").data("").result(0).build();
        }
        Optional<News> isExistNews = newsRepository.findById(request.getNewsId());
        if (isExistNews.isEmpty()) {
            return GeneralResponse.builder().message("沒有找到貼文串 ID").data("").result(0).build();
        }
        Optional<Account> isExistHostAccount = accountRepository.findById(isExistNews.get().getSendAccountId());
        if (isExistHostAccount.isEmpty()) {
            return GeneralResponse.builder().message("沒有找到貼文串的主人 ID").data("").result(0).build();
        }
        NewsReply targetNewsReply = NewsReply.builder()
                .newsId(isExistNews.get().getNewsId())
                .sendAccountId(isExistSendAccount.get().getAccountId())
                .message(request.getMessage())
                .time(request.getTime())
                .build();

        isExistNews.get().setReplyCount(isExistNews.get().getReplyCount() + 1);
        newsRepository.save(isExistNews.get());
        newsReplyRepository.save(targetNewsReply);

        if (!request.getAccountId().equals(isExistNews.get().getSendAccountId())) {
            String deviceToken = isExistHostAccount.get().getDeviceToken();
            String msgBody = isExistSendAccount.get().getName() + "回覆了你在朋友圈的分享 貼文ID " + targetNewsReply.getNewsId();
            APNsPushNotification.sendIosMsg(deviceToken, msgBody,5);
        }
        return GeneralResponse.builder().message("傳送成功").data("").result(0).build();
    }

    public GeneralResponse loadNewsReply(LoadNewsReplyRequest request) {
        Optional<News> isExistNews = newsRepository.findById(request.getNewsId());
        if (isExistNews.isEmpty()) {
            return GeneralResponse.builder().message("沒有找到貼文串 ID").data(ReplyListResponse.builder().replyList(new ArrayList<ReplyItem>()).build()).result(0).build();
        }

        List<NewsReply> newsReplyList = newsReplyRepository.findNewsReplyByNewsId(request.getNewsId());
        if (newsReplyList.isEmpty()) {
            return GeneralResponse.builder().message("沒有留言").data(ReplyListResponse.builder().replyList(new ArrayList<ReplyItem>()).build()).result(0).build();
        }

        ReplyListResponse targetReplyList = ReplyListResponse.builder().replyList(new ArrayList<ReplyItem>()).build();
        for(int i = 0;i < newsReplyList.size();i++) {
            Optional<Account> isExistAccount = accountRepository.findById(newsReplyList.get(i).getSendAccountId());
            if (isExistAccount.isEmpty()) {
                return GeneralResponse.builder().message("找不到留言的 AccountId").data(ReplyListResponse.builder().replyList(new ArrayList<ReplyItem>()).build()).result(0).build();
            }
            ReplyItem targetReplyItem = ReplyItem.builder()
                    .replyId(newsReplyList.get(i).getReplyId())
                    .accountName(isExistAccount.get().getName())
                    .accountId(isExistAccount.get().getAccountId())
                    .accountImage(isExistAccount.get().getImage())
                    .message(newsReplyList.get(i).getMessage())
                    .newsId(newsReplyList.get(i).getNewsId())
                    .time(newsReplyList.get(i).getTime())
                    .build();
            targetReplyList.getReplyList().add(targetReplyItem);
        }
        return GeneralResponse.builder().message("已找到留言").data(targetReplyList).result(0).build();
    }


    public GeneralResponse deleteReply(DeleteNewsReplyRequest request) {
        Optional<NewsReply> isExistNewsReply = newsReplyRepository.findById(request.getReplyId());
        if (isExistNewsReply.isEmpty()) {
            return GeneralResponse.builder().message("找不到留言 ID").data("").result(0).build();
        }
        Optional<News> isExistNews = newsRepository.findById(isExistNewsReply.get().getNewsId());
        if (isExistNews.isEmpty()) {
            return GeneralResponse.builder().message("找不到News ID").data("").result(0).build();
        }

        NewsReply targetNewsReply = isExistNewsReply.get();
        News targetNews = isExistNews.get();
        targetNews.setReplyCount(targetNews.getReplyCount() - 1);
        newsRepository.save(targetNews);
        newsReplyRepository.delete(targetNewsReply);
        return GeneralResponse.builder().message("刪除成功").data("").result(0).build();
    }

    public GeneralResponse editReply(EditNewsReplyRequest request) {
        Optional<NewsReply> isExistNewsReply = newsReplyRepository.findById(request.getReplyId());
        if (isExistNewsReply.isEmpty()) {
            return GeneralResponse.builder().message("找不到留言 ID").data("").result(0).build();
        }
        NewsReply targetNewsReply = isExistNewsReply.get();
        targetNewsReply.setMessage(request.getMessage());

        newsReplyRepository.save(targetNewsReply);
        return GeneralResponse.builder().message("編輯成功").data("").result(0).build();
    }
}
