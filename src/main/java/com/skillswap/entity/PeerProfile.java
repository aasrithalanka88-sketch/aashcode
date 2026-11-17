package com.skillswap.entity;

import com.skillswap.entity.enums.SkillType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "peer_profiles")
public class PeerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 150)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String aboutExperience;

    private Integer experienceYears = 1;

    @Column(length = 200)
    private String availability = "Flexible (Evenings & Weekends)";

    @Column(length = 100)
    private String location = "Remote / Online";

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "peerProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Skill> skills = new ArrayList<>();

    public PeerProfile() {
    }

    public PeerProfile(User user, String headline, String aboutExperience, Integer experienceYears, String availability, String location) {
        this.user = user;
        this.headline = headline;
        this.aboutExperience = aboutExperience;
        this.experienceYears = experienceYears != null ? experienceYears : 1;
        this.availability = availability != null ? availability : "Flexible";
        this.location = location != null ? location : "Remote";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
        skill.setPeerProfile(this);
    }

    public void removeSkill(Skill skill) {
        this.skills.remove(skill);
        skill.setPeerProfile(null);
    }

    public List<Skill> getTeachingSkills() {
        return skills.stream()
                .filter(s -> s.getType() == SkillType.TEACH)
                .collect(Collectors.toList());
    }

    public List<Skill> getLearningSkills() {
        return skills.stream()
                .filter(s -> s.getType() == SkillType.LEARN)
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getAboutExperience() {
        return aboutExperience;
    }

    public void setAboutExperience(String aboutExperience) {
        this.aboutExperience = aboutExperience;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
