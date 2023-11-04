package com.example.eyeprotext.newsReply;

import com.example.eyeprotext.news.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsReplyRepository extends JpaRepository<NewsReply, UUID> {
    @Query("SELECT a FROM NewsReply a WHERE a.newsId = ?1")
    List<NewsReply> findNewsReplyByNewsId(UUID newsId);
}
