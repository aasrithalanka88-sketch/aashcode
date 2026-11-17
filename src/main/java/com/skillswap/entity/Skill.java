package com.skillswap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.entity.enums.SkillLevel;
import com.skillswap.entity.enums.SkillType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "skills", indexes = {
    @Index(name = "idx_skill_name", columnList = "name"),
    @Index(name = "idx_skill_type", columnList = "type"),
    @Index(name = "idx_skill_category", columnList = "category")
})
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peer_profile_id", nullable = false)
    private PeerProfile peerProfile;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SkillCategory category = SkillCategory.PROGRAMMING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SkillType type = SkillType.TEACH;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SkillLevel level = SkillLevel.INTERMEDIATE;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Skill() {
    }

    public Skill(String name, SkillCategory category, SkillType type, SkillLevel level, String description) {
        this.name = name;
        this.category = category != null ? category : SkillCategory.PROGRAMMING;
        this.type = type != null ? type : SkillType.TEACH;
        this.level = level != null ? level : SkillLevel.INTERMEDIATE;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PeerProfile getPeerProfile() {
        return peerProfile;
    }

    public void setPeerProfile(PeerProfile peerProfile) {
        this.peerProfile = peerProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public SkillType getType() {
        return type;
    }

    public void setType(SkillType type) {
        this.type = type;
    }

    public SkillLevel getLevel() {
        return level;
    }

    public void setLevel(SkillLevel level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
