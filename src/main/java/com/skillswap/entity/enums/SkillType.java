package com.skillswap.entity.enums;

public enum SkillType {
    TEACH("Skills I Can Teach (Offered)"),
    LEARN("Skills I Want to Learn (Desired)");

    private final String displayName;

    SkillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
