package com.example.eyeprotext.concentrateRecord;

import com.example.eyeprotext.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConcentrateRecordRepository extends JpaRepository<ConcentrateRecord, UUID> {
    @Query("SELECT a FROM ConcentrateRecord a WHERE a.accountId = ?1")
    Optional<ConcentrateRecord> findConcentrateRecordByAccountId(String accountId);

    @Query("SELECT a FROM ConcentrateRecord a WHERE a.inviteRoomId = ?1")
    List<ConcentrateRecord> findConcentrateRecordByInviteRoomId(UUID inviteRoomId);
}

