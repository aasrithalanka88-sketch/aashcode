package com.skillswap.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class BookingRequestDto {

    @NotNull(message = "Mentor ID is required")
    private Long mentorId;

    @NotBlank(message = "Skill name is required")
    private String skillName;

    @NotBlank(message = "Please select a date for the session")
    private String scheduledDate; // YYYY-MM-DD

    @NotBlank(message = "Please select a time for the session")
    private String scheduledTime; // HH:mm

    @NotNull(message = "Please select duration")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 240, message = "Duration cannot exceed 240 minutes")
    private Integer durationMinutes = 60;

    private String meetingPlatform = "Google Meet";

    @NotBlank(message = "Please describe what topics or questions you would like to cover")
    private String topicNotes;

    public BookingRequestDto() {
    }

    public LocalDateTime resolveScheduledDateTime() {
        if (scheduledDate == null || scheduledTime == null) {
            throw new IllegalArgumentException("A session date and time are required.");
        }
        try {
            LocalDate date = LocalDate.parse(scheduledDate.trim());
            LocalTime time = LocalTime.parse(scheduledTime.trim());
            return LocalDateTime.of(date, time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Session date or time is invalid. Use YYYY-MM-DD and HH:mm.", e);
        }
    }

    // Getters and Setters
    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getMeetingPlatform() {
        return meetingPlatform;
    }

    public void setMeetingPlatform(String meetingPlatform) {
        this.meetingPlatform = meetingPlatform;
    }

    public String getTopicNotes() {
        return topicNotes;
    }

    public void setTopicNotes(String topicNotes) {
        this.topicNotes = topicNotes;
    }
}
