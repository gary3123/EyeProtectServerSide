package com.example.eyeprotext.concentrateInvite;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InviteConcentrateRoomRespository extends JpaRepository<InviteConcentrateRoom, UUID> {
}
