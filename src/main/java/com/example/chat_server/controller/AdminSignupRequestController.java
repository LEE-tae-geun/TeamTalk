package com.example.chat_server.controller;

import com.example.chat_server.dto.AdminApproveRequest;
import com.example.chat_server.dto.AdminRejectRequest;
import com.example.chat_server.dto.SignupRequestResponse;
import com.example.chat_server.entity.SignupRequest;
import com.example.chat_server.service.AdminSignupRequestService;
import com.example.chat_server.service.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/signup-requests")
public class AdminSignupRequestController {

    private final AdminSignupRequestService adminSignupRequestService;

    public AdminSignupRequestController(AdminSignupRequestService adminSignupRequestService) {
        this.adminSignupRequestService = adminSignupRequestService;
    }

    @GetMapping
    public List<SignupRequestResponse> list(@RequestParam(required = false) String status) {
        List<SignupRequest> rows = adminSignupRequestService.list(status);
        return rows.stream().map(this::toResponse).toList();
    }

    @PostMapping("/{id}/approve")
    public Map<String, Object> approve(@PathVariable("id") Long reqId,
                                       @RequestBody AdminApproveRequest body,
                                       Authentication auth) {
        Long adminMemberId = getAdminMemberId(auth);
        try {
            adminSignupRequestService.approve(reqId, body.deptId(), adminMemberId);
            return Map.of("ok", true);
        } catch (IllegalArgumentException e) {
            return Map.of("ok", false, "message", e.getMessage());
        }
    }

    @PostMapping("/{id}/reject")
    public Map<String, Object> reject(@PathVariable("id") Long reqId,
                                      @RequestBody(required = false) AdminRejectRequest body,
                                      Authentication auth) {
        Long adminMemberId = getAdminMemberId(auth);
        String reason = (body == null) ? null : body.reason();
        try {
            adminSignupRequestService.reject(reqId, adminMemberId, reason);
            return Map.of("ok", true);
        } catch (IllegalArgumentException e) {
            return Map.of("ok", false, "message", e.getMessage());
        }
    }

    private Long getAdminMemberId(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails cud) {
            return cud.getMemberId();
        }
        return null;
    }

    private SignupRequestResponse toResponse(SignupRequest r) {
        return new SignupRequestResponse(
                r.getReqId(),
                r.getName(),
                r.getEmail(),
                r.getPhoneNum(),
                r.getUsername(),
                r.getStatus(),
                r.getAssignedDeptId(),
                r.getReviewedBy(),
                r.getReviewedAt(),
                r.getRejectReason(),
                r.getCreatedAt()
        );
    }
}
