package com.skillswap.repository;

import com.skillswap.entity.SessionBooking;
import com.skillswap.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionBookingRepository extends JpaRepository<SessionBooking, Long> {

    Optional<SessionBooking> findBySessionId(String sessionId);

    Optional<SessionBooking> findBySessionIdIgnoreCase(String sessionId);

    boolean existsBySessionId(String sessionId);

    List<SessionBooking> findByLearnerIdOrderByScheduledDateTimeDesc(Long learnerId);

    List<SessionBooking> findByMentorIdOrderByScheduledDateTimeDesc(Long mentorId);

    @Query("SELECT b FROM SessionBooking b " +
           "WHERE b.learner.id = :userId OR b.mentor.id = :userId " +
           "ORDER BY b.scheduledDateTime DESC")
    List<SessionBooking> findAllUserBookings(@Param("userId") Long userId);

    long countByLearnerId(Long learnerId);

    long countByMentorIdAndStatus(Long mentorId, BookingStatus status);
}
