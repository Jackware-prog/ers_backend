package com.erwebsocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erwebsocket.model.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
    // Add custom queries if needed
}

