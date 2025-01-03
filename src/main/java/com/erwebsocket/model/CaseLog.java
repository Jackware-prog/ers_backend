package com.erwebsocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "caselog")
public class CaseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caselogid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caseid", nullable = false)
    @JsonIgnore // Ignore the "caseLogs" field in CaseHandling
    private CaseHandling caseHandling;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 255)
    private String actionTaken;

    // Constructors
    public CaseLog() {}

    public CaseLog(CaseHandling caseHandling, LocalDateTime timestamp, String actionTaken) {
        this.caseHandling = caseHandling;
        this.timestamp = timestamp;
        this.actionTaken = actionTaken;
    }

    // Getters and Setters
    public Long getCaselogid() {
        return caselogid;
    }

    public void setCaselogid(Long caselogid) {
        this.caselogid = caselogid;
    }

    public CaseHandling getCaseHandling() {
        return caseHandling;
    }

    public void setCaseHandling(CaseHandling caseHandling) {
        this.caseHandling = caseHandling;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }
}
