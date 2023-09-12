package com.example.eyeprotext.concentrateRoom;

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
@Table(name = "concentrateRoom")
public class ConcentrateRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roomId;
    private UUID sendAccountId;
    private UUID reciveAccountId;
    private String concentrateTime;
    private String restTime;
}
