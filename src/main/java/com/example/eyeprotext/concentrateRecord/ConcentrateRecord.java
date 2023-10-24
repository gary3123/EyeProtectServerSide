package com.example.eyeprotext.concentrateRecord;

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
@Table(name = "concentrateRecord")
public class ConcentrateRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID recordId;
    private UUID accountId;
    private String startTime;
    private String endTime;
    private Boolean isFinished;
    private String  concentrateTime;
    private String  restTime;
    @ElementCollection
    private List<UUID> withFriends;
}
