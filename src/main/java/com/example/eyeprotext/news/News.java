package com.example.eyeprotext.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID newsId;

    private UUID sendAccountId;

    private String title;

    @Column(name = "image", length = 5000000)
    private String image;

    private String description;

    private String time;
}
