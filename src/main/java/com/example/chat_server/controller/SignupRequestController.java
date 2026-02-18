package com.example.chat_server.controller;

import com.example.chat_server.dto.SignupRequestCreateRequest;
import com.example.chat_server.dto.SignupRequestCreateResponse;
import com.example.chat_server.service.SignupRequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SignupRequestController {

    private final SignupRequestService signupRequestService;

    public SignupRequestController(SignupRequestService signupRequestService) {
        this.signupRequestService = signupRequestService;
    }

    @PostMapping("/signup-requests")
    public SignupRequestCreateResponse create(@RequestBody SignupRequestCreateRequest req) {
        Long reqId = signupRequestService.create(req);
        return new SignupRequestCreateResponse(true, reqId, "PENDING");
    }
}