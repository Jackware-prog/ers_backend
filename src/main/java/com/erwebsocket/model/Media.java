package com.erwebsocket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportlogid", nullable = false)
    private ReportLog reportLog;

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

    public ReportLog getReportLog() {
        return reportLog;
    }

    public void setReportLog(ReportLog reportLog) {
        this.reportLog = reportLog;
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
