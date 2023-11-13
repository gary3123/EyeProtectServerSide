package com.example.eyeprotext.missionList;

import com.example.eyeprotext.account.Account;
import com.example.eyeprotext.missionCompleteCount.MissionCompleteCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface MissionListRepository
        extends JpaRepository<MissionList, UUID> {
    @Query("SELECT a FROM MissionList a WHERE a.title = ?1")
    List<MissionList> findMissionListByTitle(String title);
}


