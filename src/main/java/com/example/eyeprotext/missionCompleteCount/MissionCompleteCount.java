package com.example.eyeprotext.missionCompleteCount;

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
@Table(name = "missionComplete")
public class MissionCompleteCount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID missionID;
    private UUID accountId;
    private String title;
    private Integer count;
}
