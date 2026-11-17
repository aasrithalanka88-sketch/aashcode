package com.skillswap.entity.enums;

public enum SkillCategory {
    PROGRAMMING("Programming & Tech", "bi-code-slash", "#4f46e5"),
    DESIGN("Design & Creative", "bi-palette", "#ec4899"),
    DATA_SCIENCE("Data Science & AI", "bi-cpu", "#0ea5e9"),
    LANGUAGES("Languages", "bi-translate", "#10b981"),
    MUSIC("Music & Arts", "bi-music-note-beamed", "#f59e0b"),
    BUSINESS("Business & Finance", "bi-briefcase", "#8b5cf6"),
    MARKETING("Marketing & Sales", "bi-megaphone", "#ef4444"),
    OTHER("Other Skills", "bi-stars", "#6b7280");

    private final String displayName;
    private final String iconClass;
    private final String colorHex;

    SkillCategory(String displayName, String iconClass, String colorHex) {
        this.displayName = displayName;
        this.iconClass = iconClass;
        this.colorHex = colorHex;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getColorHex() {
        return colorHex;
    }
}
