package com.skillswap.dto;

import jakarta.validation.constraints.NotBlank;

public class CancelBookingDto {

    @NotBlank(message = "Session ID is required to cancel a booking")
    private String sessionId;

    private String cancellationReason;

    public CancelBookingDto() {
    }

    public CancelBookingDto(String sessionId, String cancellationReason) {
        this.sessionId = sessionId;
        this.cancellationReason = cancellationReason;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
