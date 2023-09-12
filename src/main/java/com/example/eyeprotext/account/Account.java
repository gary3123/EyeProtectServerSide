package com.example.eyeprotext.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    private String name;
    private String email;
    private String password;
    private String dor;
    private String deviceToken;
    @Column(name = "image", length = 50000)
    private String image;
    @ElementCollection
    private List<UUID> friendList;
}
