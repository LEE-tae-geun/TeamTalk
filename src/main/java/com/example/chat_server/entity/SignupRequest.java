package com.example.chat_server.entity;

import java.time.LocalDateTime;

public class SignupRequest {
    private Long reqId;
    private String name;
    private String email;
    private String phoneNum;
    private String username;
    private String passwordHash;
    private String status;
    private LocalDateTime createdAt;

    private Long assignedDeptId;
    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private String rejectReason;

    public Long getReqId() { return reqId; }
    public void setReqId(Long reqId) { this.reqId = reqId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getAssignedDeptId() { return assignedDeptId; }
    public void setAssignedDeptId(Long assignedDeptId) { this.assignedDeptId = assignedDeptId; }

    public Long getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
}
