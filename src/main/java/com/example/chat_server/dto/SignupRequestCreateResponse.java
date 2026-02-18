package com.example.chat_server.dto;

public class SignupRequestCreateResponse {
    private boolean ok;
    private Long reqId;
    private String status;

    public SignupRequestCreateResponse(boolean ok, Long reqId, String status) {
        this.ok = ok;
        this.reqId = reqId;
        this.status = status;
    }

    public boolean isOk() { return ok; }
    public Long getReqId() { return reqId; }
    public String getStatus() { return status; }
}