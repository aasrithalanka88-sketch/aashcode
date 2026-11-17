package com.skillswap.entity.enums;

public enum BookingStatus {
    CONFIRMED("Confirmed", "bg-success text-white"),
    COMPLETED("Completed", "bg-primary text-white"),
    CANCELLED("Cancelled", "bg-danger text-white");

    private final String label;
    private final String badgeClass;

    BookingStatus(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
