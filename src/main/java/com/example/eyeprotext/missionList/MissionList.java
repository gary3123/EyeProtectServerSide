package com.example.eyeprotext.missionList;

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
@Table(name = "missionList")
public class MissionList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID missionID;
    private String title;
    private Integer progress;
    private String progressType;
}

