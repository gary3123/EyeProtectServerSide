package com.example.eyeprotext.news;

import com.example.eyeprotext.GeneralResponse;
import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.account.AccountRepository;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecord;
import com.example.eyeprotext.concentrateRecord.ConcentrateRecordRepository;
import com.example.eyeprotext.news.Request.AddConcentrateToNewsRequest;
import com.example.eyeprotext.news.Request.AddNewsRequest;
import com.example.eyeprotext.news.Request.LoadNewsRequest;
import com.example.eyeprotext.news.response.LoadNewsResponse;
import com.example.eyeprotext.news.response.NewsItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final AccountRepository accountRepository;
    private final ConcentrateRecordRepository concentrateRecordRepository;

    public GeneralResponse addNews(AddNewsRequest request) {
        Optional<Account> isExistAccount = accountRepository.findById(request.getAccountId());
        if (!isExistAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到發送 News 的 AccountId").data("").result(0).build();
        }

        News news = News.builder()
                .sendAccountId(request.getAccountId())
                .title(request.getTitle())
                .description(request.getDescripion())
                .image(request.getImage())
                .time(request.getTime())
                .replyCount(0)
                .build();
        newsRepository.save(news);
        return GeneralResponse.builder().message("發送成功").data("").result(0).build();
    }


    public GeneralResponse addConcentrateToNews(AddConcentrateToNewsRequest request) {
       Optional<ConcentrateRecord> isExistConcentrateRecord = concentrateRecordRepository.findById(request.getConcentrateRecordId());
        if (!isExistConcentrateRecord.isPresent()) {
            return GeneralResponse.builder().message("找不到發送的 ConcentrateRecord").data("").result(0).build();
        }
        ConcentrateRecord concentrateRecord = isExistConcentrateRecord.get();

        Optional<Account> isExistAccount = accountRepository.findById(concentrateRecord.getAccountId());
        if (!isExistAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到發送的 Account").data("").result(0).build();
        }
        Account account = isExistAccount.get();

        String title = account.getName();

        if (!concentrateRecord.getWithFriends().isEmpty()) {
            title += " 和 ";

            Optional<Account> isExistHostAccount = accountRepository.findById(concentrateRecord.getHostAccountId());
            if (!isExistHostAccount.isPresent()) {
                return GeneralResponse.builder().message("找不到 HostAccount").data("").result(0).build();
            }

            Account hostAccount = isExistHostAccount.get();
            if (concentrateRecord.getAccountId().toString().equals(concentrateRecord.getHostAccountId().toString())) {
                for(int i=0; i<concentrateRecord.getWithFriends().size(); i++) {
                    Optional<Account> isExistFriendAccount = accountRepository.findById(concentrateRecord.getWithFriends().get(i));
                    if (!isExistAccount.isPresent()) {
                        return GeneralResponse.builder().message("找不到 FriendAccount").data("").result(0).build();
                    }

                    Account friendAccount = isExistFriendAccount.get();

                    if (i == concentrateRecord.getWithFriends().size() - 1) {
                        title += friendAccount.getName();
                    } else {
                        title += friendAccount.getName() + ", ";
                    }
                }
            } else {
                for(int i=0; i<concentrateRecord.getWithFriends().size(); i++) {
                    Optional<Account> isExistFriendAccount = accountRepository.findById(concentrateRecord.getWithFriends().get(i));
                    if (!isExistAccount.isPresent()) {
                        return GeneralResponse.builder().message("找不到 FriendAccount").data("").result(0).build();
                    }
                    Account friendAccount = isExistFriendAccount.get();

                    if (i == concentrateRecord.getWithFriends().size() - 1) {
                        title += friendAccount.getName() +  ", " + hostAccount.getName();
                    } else {
                        title += friendAccount.getName() + ", ";
                    }
                }
            }
        }

        title += " 完成了 " + concentrateRecord.getConcentrateTime() + " 的專注任務";

        News news = News.builder()
                .sendAccountId(concentrateRecord.getAccountId())
                .title(title)
                .description(concentrateRecord.getDescription())
                .image(concentrateRecord.getImage())
                .time(concentrateRecord.getEndTime())
                .replyCount(0)
                .build();

        newsRepository.save(news);

        return GeneralResponse.builder().message("發送成功").data("").result(0).build();
    }

    public GeneralResponse loadNews(LoadNewsRequest request) {
        Optional<Account> isExistAccount = accountRepository.findById(request.getAccountId());
        if (!isExistAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到 AccountId").data(LoadNewsResponse.builder().build()).result(0).build();
        }
        List<NewsItem> targetNewsItem = new ArrayList<>();

        Account account = isExistAccount.get();
        List<UUID> targetAccountList = account.getFriendList();
        targetAccountList.add(request.getAccountId());
        for(int i = 0;i < targetAccountList.size();i++) {
            Optional<Account> isExistFriendAccount = accountRepository.findById(targetAccountList.get(i));
            if (!isExistFriendAccount.isPresent()) {
                return GeneralResponse.builder().message("找不到 FriendAccountId").data(LoadNewsResponse.builder().build()).result(0).build();
            }

            Account friendAccount = isExistFriendAccount.get();
            List<News> tragetNewsList = newsRepository.findNewsBySendAccountId(friendAccount.getAccountId());

            for(int a = 0; a < tragetNewsList.size(); a++) {
                Optional<News> isExistNews = newsRepository.findById(tragetNewsList.get(a).getNewsId());
                if (!isExistNews.isPresent()) {
                    return GeneralResponse.builder().message("找不到 NewsId").data(LoadNewsResponse.builder().build()).result(0).build();
                }
                News targetNew = isExistNews.get();
                targetNewsItem.add(NewsItem.builder()
                        .NewsPicture(targetNew.getImage())
                        .newsId(targetNew.getNewsId())
                        .description(targetNew.getDescription())
                        .sendAccountId(targetNew.getSendAccountId())
                        .title(targetNew.getTitle())
                        .sendAccountImage(friendAccount.getImage())
                        .sendAccountName(friendAccount.getName())
                        .time(targetNew.getTime())
                        .replyCount(targetNew.getReplyCount())
                        .build());
            }
        }
        LoadNewsResponse response = LoadNewsResponse.builder().newsItems(targetNewsItem).build();
        return GeneralResponse.builder().message("已搜尋所有的 News").data(response).result(0).build();
    }

    public GeneralResponse loadOnePersonNews(LoadNewsRequest request) {
        Optional<Account> isExistAccount = accountRepository.findById(request.getAccountId());
        if (!isExistAccount.isPresent()) {
            return GeneralResponse.builder().message("找不到 AccountId").data(LoadNewsResponse.builder().build()).result(0).build();
        }
        Account account = isExistAccount.get();

        List<NewsItem> targetNewsItem = new ArrayList<>();
        List<News> tragetNewsList = newsRepository.findNewsBySendAccountId(account.getAccountId());

        for(int i = 0; i < tragetNewsList.size(); i++) {
            Optional<News> isExistNews = newsRepository.findById(tragetNewsList.get(i).getNewsId());
            if (!isExistNews.isPresent()) {
                return GeneralResponse.builder().message("找不到 NewsId").data(LoadNewsResponse.builder().build()).result(0).build();
            }
            News targetNew = isExistNews.get();
            targetNewsItem.add(NewsItem.builder()
                    .NewsPicture(targetNew.getImage())
                    .newsId(targetNew.getNewsId())
                    .description(targetNew.getDescription())
                    .sendAccountId(targetNew.getSendAccountId())
                    .title(targetNew.getTitle())
                    .sendAccountImage(account.getImage())
                    .sendAccountName(account.getName())
                    .time(targetNew.getTime())
                    .replyCount(targetNew.getReplyCount())
                    .build());
        }
        LoadNewsResponse response = LoadNewsResponse.builder().newsItems(targetNewsItem).build();
        return GeneralResponse.builder().message("已搜尋所有的 News").data(response).result(0).build();
    }
}
