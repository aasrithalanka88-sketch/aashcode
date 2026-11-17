package com.skillswap.entity;

import com.skillswap.entity.enums.BookingStatus;
import jakarta.persistence.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Entity
@Table(name = "session_bookings", indexes = {
    @Index(name = "idx_booking_session_id", columnList = "sessionId", unique = true),
    @Index(name = "idx_booking_learner", columnList = "learner_id"),
    @Index(name = "idx_booking_mentor", columnList = "mentor_id")
})
public class SessionBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String sessionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "learner_id", nullable = false)
    private User learner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @Column(nullable = false, length = 100)
    private String skillName;

    @Column(nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(nullable = false)
    private Integer durationMinutes = 60;

    @Column(length = 50)
    private String meetingPlatform = "Google Meet";

    @Column(length = 255)
    private String meetingLink;

    @Column(columnDefinition = "TEXT")
    private String topicNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(columnDefinition = "TEXT")
    private String cancellationReason;

    private LocalDateTime cancelledAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public SessionBooking() {
    }

    public SessionBooking(User learner, User mentor, String skillName, LocalDateTime scheduledDateTime,
                          Integer durationMinutes, String meetingPlatform, String topicNotes) {
        this.sessionId = generateSessionId();
        this.learner = learner;
        this.mentor = mentor;
        this.skillName = skillName;
        this.scheduledDateTime = scheduledDateTime;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 60;
        this.meetingPlatform = meetingPlatform != null && !meetingPlatform.isBlank() ? meetingPlatform : "Google Meet";
        this.meetingLink = "https://meet.skillswap.local/room/" + this.sessionId.toLowerCase();
        this.topicNotes = topicNotes;
        this.status = BookingStatus.CONFIRMED;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.sessionId == null || this.sessionId.isBlank()) {
            this.sessionId = generateSessionId();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.meetingLink == null && this.sessionId != null) {
            this.meetingLink = "https://meet.skillswap.local/room/" + this.sessionId.toLowerCase();
        }
    }

    /**
     * Generates a unique, collision-resistant, human-friendly Session ID.
     * Format: SS-XXXX-XXXX (e.g. SS-7K9A-4M2B)
     */
    public static String generateSessionId() {
        final String ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder part1 = new StringBuilder(4);
        StringBuilder part2 = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            part1.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
            part2.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return "SS-" + part1 + "-" + part2;
    }

    public boolean isCancellable() {
        return this.status == BookingStatus.CONFIRMED;
    }

    public void cancel(String reason) {
        this.status = BookingStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancelledAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = BookingStatus.COMPLETED;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getLearner() {
        return learner;
    }

    public void setLearner(User learner) {
        this.learner = learner;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
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

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getTopicNotes() {
        return topicNotes;
    }

    public void setTopicNotes(String topicNotes) {
        this.topicNotes = topicNotes;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
