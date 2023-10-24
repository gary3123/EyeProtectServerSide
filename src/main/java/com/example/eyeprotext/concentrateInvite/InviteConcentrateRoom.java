package com.example.eyeprotext.concentrateInvite;

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
@Table(name = "inviteConcentrateRoom")
public class InviteConcentrateRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID inviteRoomId;
    private UUID sendAccountId;

    @ElementCollection
    private List<UUID> reciveAccountId;

    @ElementCollection
    private List<UUID> joinAccountId;
}
