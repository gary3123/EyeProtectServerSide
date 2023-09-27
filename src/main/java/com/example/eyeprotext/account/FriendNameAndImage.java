package com.example.eyeprotext.account;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendNameAndImage {
    private UUID accountId;
    private String name;
    @Column(name = "image", length = 5000000)
    private String image;
}
