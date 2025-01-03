package com.erwebsocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "casehandling")
public class CaseHandling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseid;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emergencyid", nullable = false, unique = true)
    private Emergency emergency;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminid", nullable = false)
    @JsonIgnore
    private Admin admin;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "caseHandling", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseLog> caseLogs;

    // Constructors
    public CaseHandling() {}

    public CaseHandling(Emergency emergency, Admin admin, LocalDateTime timestamp) {
        this.emergency = emergency;
        this.admin = admin;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getCaseid() {
        return caseid;
    }

    public void setCaseid(Long caseid) {
        this.caseid = caseid;
    }

    public Emergency getEmergency() {
        return emergency;
    }

    public void setEmergency(Emergency emergency) {
        this.emergency = emergency;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<CaseLog> getCaseLogs() {
        return caseLogs;
    }

    public void setCaseLogs(List<CaseLog> caseLogs) {
        this.caseLogs = caseLogs;
    }
}
