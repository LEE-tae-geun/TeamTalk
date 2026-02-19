package com.example.chat_server.dto;

public class ApiErrorResponse {
    private boolean ok;
    private String message;

    public ApiErrorResponse(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
}