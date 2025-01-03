package com.erwebsocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erwebsocket.model.EmergencyLog;

public interface EmergencyLogRepository extends JpaRepository<EmergencyLog, Long> {
}

