package com.skillswap.dto;

import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.entity.enums.SkillLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PeerRegistrationDto {

    @NotBlank(message = "Please provide a headline (e.g. 'Senior Java Developer & Mentor')")
    @Size(max = 150, message = "Headline must be under 150 characters")
    private String headline;

    private String aboutExperience;

    @Min(value = 0, message = "Experience years cannot be negative")
    private Integer experienceYears = 2;

    private String availability = "Flexible (Evenings & Weekends)";

    private String location = "Remote / Online";

    // Skills I can TEACH (Offered Skills)
    @NotBlank(message = "Please list at least one skill you can teach (comma separated)")
    private String teachingSkills;

    private SkillCategory teachingCategory = SkillCategory.PROGRAMMING;

    private SkillLevel teachingLevel = SkillLevel.ADVANCED;

    private String teachingDescription;

    // Skills I want to LEARN (Desired Skills)
    @NotBlank(message = "Please list at least one skill you want to learn (comma separated)")
    private String learningSkills;

    private SkillCategory learningCategory = SkillCategory.DESIGN;

    private SkillLevel learningLevel = SkillLevel.BEGINNER;

    private String learningDescription;

    public PeerRegistrationDto() {
    }

    // Getters and Setters
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

    public String getTeachingSkills() {
        return teachingSkills;
    }

    public void setTeachingSkills(String teachingSkills) {
        this.teachingSkills = teachingSkills;
    }

    public SkillCategory getTeachingCategory() {
        return teachingCategory;
    }

    public void setTeachingCategory(SkillCategory teachingCategory) {
        this.teachingCategory = teachingCategory;
    }

    public SkillLevel getTeachingLevel() {
        return teachingLevel;
    }

    public void setTeachingLevel(SkillLevel teachingLevel) {
        this.teachingLevel = teachingLevel;
    }

    public String getTeachingDescription() {
        return teachingDescription;
    }

    public void setTeachingDescription(String teachingDescription) {
        this.teachingDescription = teachingDescription;
    }

    public String getLearningSkills() {
        return learningSkills;
    }

    public void setLearningSkills(String learningSkills) {
        this.learningSkills = learningSkills;
    }

    public SkillCategory getLearningCategory() {
        return learningCategory;
    }

    public void setLearningCategory(SkillCategory learningCategory) {
        this.learningCategory = learningCategory;
    }

    public SkillLevel getLearningLevel() {
        return learningLevel;
    }

    public void setLearningLevel(SkillLevel learningLevel) {
        this.learningLevel = learningLevel;
    }

    public String getLearningDescription() {
        return learningDescription;
    }

    public void setLearningDescription(String learningDescription) {
        this.learningDescription = learningDescription;
    }
}
