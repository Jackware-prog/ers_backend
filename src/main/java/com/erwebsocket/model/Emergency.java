package com.erwebsocket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emergency")
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emergencyid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", nullable = false)
    @JsonManagedReference
    private User user;

    @Column(nullable = false)
    private String emergencyType;

    @Column(nullable = false)
    private Boolean isVictim;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private LocalDateTime closeTimestamp;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean isPublish;

    @Column(nullable = false)
    private Boolean isFalseMessage;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @OneToMany(mappedBy = "emergency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmergencyLog> emergencyLogs;



    // Getters and Setters
    public Long getEmergencyid() {
        return emergencyid;
    }

    public void setEmergencyid(Long emergencyid) {
        this.emergencyid = emergencyid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public Boolean getIsVictim() {
        return isVictim;
    }

    public void setIsVictim(Boolean isVictim) {
        this.isVictim = isVictim;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getCloseTimestamp() {
        return closeTimestamp;
    }

    public void setCloseTimestamp(LocalDateTime closeTimestamp) {
        this.closeTimestamp = closeTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Boolean isPublish) {
        this.isPublish = isPublish;
    }

    public Boolean getIsFalseMessage() {
        return isFalseMessage;
    }

    public void setIsFalseMessage(Boolean isFalseMessage) {
        this.isFalseMessage = isFalseMessage;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public List<EmergencyLog> getReportLogs() {
        return emergencyLogs;
    }

    public void setReportLogs(List<EmergencyLog> emergencyLogs) {
        this.emergencyLogs = emergencyLogs;
    }
}
