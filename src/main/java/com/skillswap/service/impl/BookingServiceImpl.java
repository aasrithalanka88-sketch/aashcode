package com.skillswap.service.impl;

import com.skillswap.dto.BookingRequestDto;
import com.skillswap.entity.SessionBooking;
import com.skillswap.entity.User;
import com.skillswap.entity.enums.BookingStatus;
import com.skillswap.repository.SessionBookingRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final SessionBookingRepository sessionBookingRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(SessionBookingRepository sessionBookingRepository, UserRepository userRepository) {
        this.sessionBookingRepository = sessionBookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SessionBooking bookSession(Long learnerUserId, BookingRequestDto dto) {
        if (learnerUserId.equals(dto.getMentorId())) {
            throw new IllegalArgumentException("You cannot book a skill swap session with yourself!");
        }

        User learner = userRepository.findById(learnerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Learner user not found with id: " + learnerUserId));

        User mentor = userRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new IllegalArgumentException("Mentor peer not found with id: " + dto.getMentorId()));

        LocalDateTime scheduledDateTime = dto.resolveScheduledDateTime();

        // Generate unique Session ID
        String sessionId;
        int attempts = 0;
        do {
            sessionId = SessionBooking.generateSessionId();
            attempts++;
            if (attempts > 10) {
                sessionId = "SS-" + System.currentTimeMillis();
                break;
            }
        } while (sessionBookingRepository.existsBySessionId(sessionId));

        SessionBooking booking = new SessionBooking(
                learner,
                mentor,
                dto.getSkillName().trim(),
                scheduledDateTime,
                dto.getDurationMinutes(),
                dto.getMeetingPlatform(),
                dto.getTopicNotes()
        );
        booking.setSessionId(sessionId);
        booking.setStatus(BookingStatus.CONFIRMED);

        return sessionBookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionBooking getBookingBySessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("Session ID cannot be empty");
        }
        return sessionBookingRepository.findBySessionIdIgnoreCase(sessionId.trim())
                .orElseThrow(() -> new IllegalArgumentException("No booking found with Session ID: " + sessionId.trim().toUpperCase()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionBooking> findBookingBySessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return Optional.empty();
        }
        return sessionBookingRepository.findBySessionIdIgnoreCase(sessionId.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionBooking> getLearnerBookings(Long learnerUserId) {
        return sessionBookingRepository.findByLearnerIdOrderByScheduledDateTimeDesc(learnerUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionBooking> getMentorBookings(Long mentorUserId) {
        return sessionBookingRepository.findByMentorIdOrderByScheduledDateTimeDesc(mentorUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionBooking> getAllBookingsForUser(Long userId) {
        return sessionBookingRepository.findAllUserBookings(userId);
    }

    @Override
    public SessionBooking cancelSession(String sessionId, String reason, Long requestingUserId) {
        SessionBooking booking = getBookingBySessionId(sessionId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Session " + booking.getSessionId() + " is already cancelled.");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Session " + booking.getSessionId() + " is already completed and cannot be cancelled.");
        }

        // If requestingUserId is supplied, check permission
        if (requestingUserId != null) {
            boolean isParticipant = booking.getLearner().getId().equals(requestingUserId) ||
                                    booking.getMentor().getId().equals(requestingUserId);
            if (!isParticipant) {
                throw new SecurityException("You do not have permission to cancel this session.");
            }
        }

        String cancelReason = (reason != null && !reason.isBlank()) ? reason.trim() : "Cancelled by peer request";
        booking.cancel(cancelReason);

        return sessionBookingRepository.save(booking);
    }

    @Override
    public SessionBooking completeSession(String sessionId, Long requestingUserId) {
        SessionBooking booking = getBookingBySessionId(sessionId);
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete a cancelled session.");
        }
        booking.complete();
        return sessionBookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionBooking> getAllBookings() {
        return sessionBookingRepository.findAll();
    }
}
