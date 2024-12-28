package com.erwebsocket.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "report_log")
public class ReportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportlogid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportid", nullable = false)
    private Report report;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String detailedAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "reportLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> media;

    // Getters and Setters
    public Long getReportlogid() {
        return reportlogid;
    }

    public void setReportlogid(Long reportlogid) {
        this.reportlogid = reportlogid;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
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
