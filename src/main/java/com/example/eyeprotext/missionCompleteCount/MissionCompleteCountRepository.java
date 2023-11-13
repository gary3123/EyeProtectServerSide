package com.example.eyeprotext.missionCompleteCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MissionCompleteCountRepository extends JpaRepository<MissionCompleteCount, UUID> {
    @Query("SELECT a FROM MissionCompleteCount a WHERE a.date LIKE %?1%")
    List<MissionCompleteCount> findMissionCompleteCountByDate(String date);

    @Query("SELECT a FROM MissionCompleteCount a WHERE a.accountId = ?1 AND a.date LIKE %?2%")
    List<MissionCompleteCount> findMissionCompleteCountByAccountIdAndDate(UUID accountId, String date);

    @Query("SELECT a FROM MissionCompleteCount a WHERE a.accountId = ?1 AND a.missionID = ?2 AND a.date LIKE %?3%")
    Optional<MissionCompleteCount> findMissionCompleteCountByAccountIdAndMissionIdAndDate(UUID accountId, UUID missionId, String date);
}
