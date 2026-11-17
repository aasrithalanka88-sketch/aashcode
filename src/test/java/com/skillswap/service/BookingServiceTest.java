package com.skillswap.service;

import com.skillswap.dto.BookingRequestDto;
import com.skillswap.entity.SessionBooking;
import com.skillswap.entity.User;
import com.skillswap.entity.enums.BookingStatus;
import com.skillswap.repository.SessionBookingRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private SessionBookingRepository sessionBookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User learner;
    private User mentor;

    @BeforeEach
    void setUp() {
        learner = new User("Learner User", "learner@skillswap.com", "pass", "Learner Bio");
        learner.setId(1L);

        mentor = new User("Mentor Peer", "mentor@skillswap.com", "pass", "Mentor Bio");
        mentor.setId(2L);
    }

    @Test
    void testBookSessionGeneratesUniqueSessionId() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setMentorId(2L);
        dto.setSkillName("Java");
        dto.setScheduledDate(LocalDate.now().plusDays(2).toString());
        dto.setScheduledTime("14:00");
        dto.setDurationMinutes(60);
        dto.setMeetingPlatform("Google Meet");
        dto.setTopicNotes("Deep dive into Spring Data JPA");

        when(userRepository.findById(1L)).thenReturn(Optional.of(learner));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mentor));
        when(sessionBookingRepository.existsBySessionId(anyString())).thenReturn(false);
        when(sessionBookingRepository.save(any(SessionBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessionBooking result = bookingService.bookSession(1L, dto);

        assertNotNull(result);
        assertNotNull(result.getSessionId());
        assertTrue(result.getSessionId().startsWith("SS-"));
        assertEquals("Java", result.getSkillName());
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(learner, result.getLearner());
        assertEquals(mentor, result.getMentor());

        verify(sessionBookingRepository, times(1)).save(any(SessionBooking.class));
    }

    @Test
    void testBookSessionThrowsWhenBookingSelf() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setMentorId(1L);
        dto.setSkillName("Java");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.bookSession(1L, dto));

        assertTrue(exception.getMessage().contains("cannot book a skill swap session with yourself"));
    }

    @Test
    void testCancelSessionBySessionId() {
        SessionBooking booking = new SessionBooking(learner, mentor, "Java",
                LocalDateTime.now().plusDays(1), 60, "Google Meet", "Intro");
        booking.setSessionId("SS-TEST-1234");
        booking.setStatus(BookingStatus.CONFIRMED);

        when(sessionBookingRepository.findBySessionIdIgnoreCase("SS-TEST-1234")).thenReturn(Optional.of(booking));
        when(sessionBookingRepository.save(any(SessionBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessionBooking cancelled = bookingService.cancelSession("SS-TEST-1234", "Schedule conflict", 1L);

        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
        assertEquals("Schedule conflict", cancelled.getCancellationReason());
        assertNotNull(cancelled.getCancelledAt());
    }

    @Test
    void testCancelAlreadyCancelledSessionThrows() {
        SessionBooking booking = new SessionBooking(learner, mentor, "Java",
                LocalDateTime.now().plusDays(1), 60, "Google Meet", "Intro");
        booking.setSessionId("SS-TEST-9999");
        booking.setStatus(BookingStatus.CANCELLED);

        when(sessionBookingRepository.findBySessionIdIgnoreCase("SS-TEST-9999")).thenReturn(Optional.of(booking));

        assertThrows(IllegalStateException.class, () ->
                bookingService.cancelSession("SS-TEST-9999", "Another cancellation", 1L));
    }

    @Test
    void invalidScheduledDateIsRejectedInsteadOfSilentlyChanged() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setScheduledDate("not-a-date");
        dto.setScheduledTime("14:00");

        assertThrows(IllegalArgumentException.class, dto::resolveScheduledDateTime);
    }
}
