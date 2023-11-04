package com.example.eyeprotext.newsReply;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "newsReply")
public class NewsReply {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID replyId;

    private UUID newsId;

    private UUID sendAccountId;

    private String message;

    private String time;
}
