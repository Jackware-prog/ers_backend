package com.erwebsocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emergency_log")
public class EmergencyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emergencylogid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergencyid", nullable = false)
    @JsonIgnore
    private Emergency emergency;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String detailedAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "emergencyLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> media;

    // Getters and Setters
    public Long getEmergencylogid() {
        return emergencylogid;
    }

    public void setEmergencylogid(Long emergencylogid) {
        this.emergencylogid = emergencylogid;
    }

    public Emergency getEmergency() {
        return emergency;
    }

    public void setEmergency(Emergency emergency) {
        this.emergency = emergency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }
}
