package com.erwebsocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergencylogid", nullable = false)
    @JsonIgnore
    private EmergencyLog emergencyLog;

    @Column(nullable = false)
    private String mediaType;

    @Column(nullable = false)
    private String mediaPath;

    // Getters and Setters
    public Long getMediaid() {
        return mediaid;
    }

    public void setMediaid(Long mediaid) {
        this.mediaid = mediaid;
    }

    public EmergencyLog getEmergencyLog() {
        return emergencyLog;
    }

    public void setEmergencyLog(EmergencyLog emergencyLog) {
        this.emergencyLog = emergencyLog;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
}
