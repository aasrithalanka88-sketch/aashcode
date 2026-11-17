package com.skillswap.entity.enums;

public enum SkillLevel {
    BEGINNER("Beginner", "badge-info"),
    INTERMEDIATE("Intermediate", "badge-primary"),
    ADVANCED("Advanced", "badge-success"),
    EXPERT("Expert", "badge-warning");

    private final String label;
    private final String badgeClass;

    SkillLevel(String label, String badgeClass) {
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
