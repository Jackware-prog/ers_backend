package com.erwebsocket.repository;

import com.erwebsocket.model.CaseHandling;
import com.erwebsocket.model.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CaseHandlingRepository extends JpaRepository<CaseHandling, Long> {
    @Query("SELECT c FROM CaseHandling c WHERE c.admin.adminid = :adminid AND c.emergency.status = 'active' ORDER BY c.timestamp desc")
    List<CaseHandling> findByAdminidAndActiveEmergency(Long adminid);

    @Query("SELECT c FROM CaseHandling c WHERE c.admin.adminid = :adminid AND c.emergency.status = 'closed' ORDER BY c.timestamp desc")
    List<CaseHandling> findByAdminidAndClosedEmergency(Long adminid);

    @Query("SELECT ch FROM CaseHandling ch JOIN FETCH ch.emergency e JOIN FETCH e.user WHERE ch.caseid = :caseid ORDER BY ch.timestamp desc")
    CaseHandling findBycaseidWithUser(Long caseid);

    Optional<CaseHandling> findByCaseid(Long caseid);
}