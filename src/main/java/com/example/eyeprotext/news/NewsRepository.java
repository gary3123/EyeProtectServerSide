package com.example.eyeprotext.news;


import com.example.eyeprotext.concentrateRecord.ConcentrateRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository  extends JpaRepository<News, UUID> {
    @Query("SELECT a FROM News a WHERE a.sendAccountId = ?1")
    List<News> findNewsBySendAccountId(UUID sendAccountId);
}
