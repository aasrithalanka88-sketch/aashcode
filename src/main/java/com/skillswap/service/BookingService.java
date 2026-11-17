
package com.skillswap.service;

import com.skillswap.dto.BookingRequestDto;
import com.skillswap.entity.SessionBooking;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    SessionBooking bookSession(Long learnerUserId, BookingRequestDto dto);
    SessionBooking getBookingBySessionId(String sessionId);
    Optional<SessionBooking> findBookingBySessionId(String sessionId);
    List<SessionBooking> getLearnerBookings(Long learnerUserId);
    List<SessionBooking> getMentorBookings(Long mentorUserId);
    List<SessionBooking> getAllBookingsForUser(Long userId);
    SessionBooking cancelSession(String sessionId, String reason, Long requestingUserId);
    SessionBooking completeSession(String sessionId, Long requestingUserId);
    List<SessionBooking> getAllBookings();
}
