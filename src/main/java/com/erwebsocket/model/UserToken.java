package com.erwebsocket.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = true)
    private User userid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminid", nullable = true)
    private Admin adminid;

    @Column(nullable = false, unique = true)
    private String fcmToken;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public UserToken() {}

    public UserToken(User userid, Admin adminid, String fcmToken, LocalDateTime updatedAt) {
        this.userid = userid;
        this.adminid = adminid;
        this.fcmToken = fcmToken;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return userid;
    }

    public void setUser(User user) {
        this.userid = user;
    }

    public Admin getAdmin() {
        return adminid;
    }

    public void setAdmin(Admin admin) {
        this.adminid = admin;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
