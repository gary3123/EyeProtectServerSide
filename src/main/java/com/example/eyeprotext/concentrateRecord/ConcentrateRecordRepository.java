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
    List<ConcentrateRecord> findConcentrateRecordByAccountId(UUID accountId);

    @Query("SELECT a FROM ConcentrateRecord a WHERE a.inviteRoomId = ?1")
    List<ConcentrateRecord> findConcentrateRecordByInviteRoomId(UUID inviteRoomId);

    @Query("SELECT a FROM ConcentrateRecord a WHERE a.inviteRoomId = ?1 AND a.accountId = ?2")
    Optional<ConcentrateRecord> findConcentrateRecordByInviteRoomIdAndAccountId(UUID inviteRoomId, UUID accountId);

    @Query("SELECT a FROM ConcentrateRecord a WHERE a.accountId = ?1 AND a.endTime LIKE %?2%")
    List<ConcentrateRecord> findConcentrateRecordByAccountIdAndDate(UUID accountId, String date);
}

