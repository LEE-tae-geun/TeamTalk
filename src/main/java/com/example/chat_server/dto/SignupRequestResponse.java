package com.example.chat_server.dto;

import java.time.LocalDateTime;

public record SignupRequestResponse(
        Long reqId,
        String name,
        String email,
        String phoneNum,
        String username,
        String status,
        Long assignedDeptId,
        Long reviewedBy,
        LocalDateTime reviewedAt,
        String rejectReason,
        LocalDateTime createdAt
) {
}
