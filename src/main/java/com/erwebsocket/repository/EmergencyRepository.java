package com.erwebsocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erwebsocket.model.Emergency;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    List<Emergency> findByUserUseridAndTimestampAfterOrderByTimestampDesc(Long userid, LocalDateTime timestamp); // Fetch by userid within certain date
    Emergency findByEmergencyid(Long emergencyid); // Fetch by reportid
    List<Emergency> findByIsPublishTrueAndTimestampAfterOrderByTimestampDesc(LocalDateTime timestamp);// Fetch by timestamp

    @Query("SELECT e FROM Emergency e WHERE e.status = 'active' AND e.emergencyid NOT IN (SELECT c.emergency.emergencyid FROM CaseHandling c)")
    List<Emergency> findUnhandledReports();

}

