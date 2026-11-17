package com.skillswap.controller;

import com.skillswap.dto.BookingRequestDto;
import com.skillswap.dto.CancelBookingDto;
import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.SessionBooking;
import com.skillswap.entity.User;
import com.skillswap.entity.enums.BookingStatus;
import com.skillswap.service.BookingService;
import com.skillswap.service.PeerService;
import com.skillswap.service.UserService;
import com.skillswap.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PeerService peerService;
    private final UserService userService;

    public BookingController(BookingService bookingService, PeerService peerService, UserService userService) {
        this.bookingService = bookingService;
        this.peerService = peerService;
        this.userService = userService;
    }

    /**
     * Requirement 6: Session Booking Form
     */
    @GetMapping("/new")
    public String showBookingForm(@RequestParam("mentorId") Long mentorId,
                                  @RequestParam(value = "skill", required = false) String skill,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Long currentUserId = SessionHelper.getLoggedInUserId(session);
        if (currentUserId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to book a skill session with this peer.");
            return "redirect:/login";
        }

        if (currentUserId.equals(mentorId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot book a learning session with yourself.");
            return "redirect:/peers";
        }

        User mentorUser = userService.getById(mentorId);
        PeerProfile mentorPeer = peerService.getPeerByUserId(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Peer profile not found for mentor"));

        BookingRequestDto bookingDto = new BookingRequestDto();
        bookingDto.setMentorId(mentorId);
        if (skill != null && !skill.isBlank()) {
            bookingDto.setSkillName(skill.trim());
        }
        // Default tomorrow at 18:00
        bookingDto.setScheduledDate(LocalDate.now().plusDays(1).toString());
        bookingDto.setScheduledTime("18:00");

        model.addAttribute("mentor", mentorUser);
        model.addAttribute("mentorPeer", mentorPeer);
        model.addAttribute("bookingDto", bookingDto);
        return "booking/book";
    }

    /**
     * Requirement 6: Process Session Booking & Generate Unique Session ID
     */
    @PostMapping("/new")
    public String processBooking(@Valid @ModelAttribute("bookingDto") BookingRequestDto bookingDto,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Long currentUserId = SessionHelper.getLoggedInUserId(session);
        if (currentUserId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to complete your booking.");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            User mentorUser = userService.getById(bookingDto.getMentorId());
            PeerProfile mentorPeer = peerService.getPeerByUserId(bookingDto.getMentorId()).orElse(null);
            model.addAttribute("mentor", mentorUser);
            model.addAttribute("mentorPeer", mentorPeer);
            return "booking/book";
        }

        try {
            SessionBooking booking = bookingService.bookSession(currentUserId, bookingDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Session successfully booked! Unique Session ID generated: " + booking.getSessionId());
            return "redirect:/bookings/confirmed/" + booking.getSessionId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bookings/new?mentorId=" + bookingDto.getMentorId();
        }
    }

    /**
     * Requirement 6: Display Unique Session ID and Booking Confirmation
     */
    @GetMapping("/confirmed/{sessionId}")
    public String showBookingConfirmation(@PathVariable String sessionId, Model model) {
        SessionBooking booking = bookingService.getBookingBySessionId(sessionId);
        model.addAttribute("booking", booking);
        return "booking/success";
    }

    /**
     * Requirement 7: Booking History Dashboard
     * Displays all sessions booked by or with the logged-in peer.
     */
    @GetMapping("/history")
    public String showBookingHistory(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long currentUserId = SessionHelper.getLoggedInUserId(session);
        if (currentUserId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to view your session booking history.");
            return "redirect:/login";
        }

        List<SessionBooking> learnerBookings = bookingService.getLearnerBookings(currentUserId);
        List<SessionBooking> mentorBookings = bookingService.getMentorBookings(currentUserId);

        long confirmedCount = learnerBookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count()
                            + mentorBookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count();

        long completedCount = learnerBookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count()
                            + mentorBookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count();

        long cancelledCount = learnerBookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELLED).count()
                            + mentorBookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELLED).count();

        model.addAttribute("learnerBookings", learnerBookings);
        model.addAttribute("mentorBookings", mentorBookings);
        model.addAttribute("confirmedCount", confirmedCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("cancelledCount", cancelledCount);
        model.addAttribute("totalBookings", learnerBookings.size() + mentorBookings.size());

        return "booking/history";
    }

    /**
     * Requirement 8: Session Cancellation Page (Form to enter Session ID)
     */
    @GetMapping("/cancel")
    public String showCancelForm(@RequestParam(value = "sessionId", required = false) String sessionId,
                                 Model model) {
        CancelBookingDto cancelDto = new CancelBookingDto();
        if (sessionId != null && !sessionId.isBlank()) {
            cancelDto.setSessionId(sessionId.trim());
        }
        model.addAttribute("cancelDto", cancelDto);
        return "booking/cancel";
    }

    /**
     * Requirement 8: Cancel Session by Unique Session ID
     */
    @PostMapping("/cancel")
    public String processCancellation(@Valid @ModelAttribute("cancelDto") CancelBookingDto cancelDto,
                                      BindingResult bindingResult,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "booking/cancel";
        }

        Long currentUserId = SessionHelper.getLoggedInUserId(session);

        try {
            SessionBooking cancelled = bookingService.cancelSession(
                    cancelDto.getSessionId(),
                    cancelDto.getCancellationReason(),
                    currentUserId
            );
            redirectAttributes.addFlashAttribute("successMessage",
                    "Session " + cancelled.getSessionId() + " (" + cancelled.getSkillName() + ") has been successfully cancelled.");
            return "redirect:/bookings/history";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cancellation failed: " + e.getMessage());
            return "redirect:/bookings/cancel?sessionId=" + cancelDto.getSessionId();
        }
    }

    /**
     * Requirement 8: Direct Cancel from History Table
     */
    @PostMapping("/cancel/{sessionId}")
    public String directCancel(@PathVariable String sessionId,
                               @RequestParam(value = "reason", required = false) String reason,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Long currentUserId = SessionHelper.getLoggedInUserId(session);
        if (currentUserId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to cancel sessions.");
            return "redirect:/login";
        }

        try {
            SessionBooking cancelled = bookingService.cancelSession(sessionId, reason, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Session " + cancelled.getSessionId() + " has been cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not cancel session: " + e.getMessage());
        }
        return "redirect:/bookings/history";
    }
}
