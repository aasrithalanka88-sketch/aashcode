package com.skillswap.controller;

import com.skillswap.dto.BookingRequestDto;
import com.skillswap.dto.CancelBookingDto;
import com.skillswap.dto.WantedSkillGroupDto;
import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.SessionBooking;
import com.skillswap.entity.Skill;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.service.BookingService;
import com.skillswap.service.PeerService;
import com.skillswap.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private final PeerService peerService;
    private final SkillService skillService;
    private final BookingService bookingService;

    public ApiController(PeerService peerService, SkillService skillService, BookingService bookingService) {
        this.peerService = peerService;
        this.skillService = skillService;
        this.bookingService = bookingService;
    }

    /**
     * Requirement 3: View All Peers (REST API)
     */
    @GetMapping("/peers")
    public ResponseEntity<List<PeerProfile>> getAllPeers(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(peerService.searchPeers(search));
        }
        return ResponseEntity.ok(peerService.getAllActivePeers());
    }

    @GetMapping("/peers/{id}")
    public ResponseEntity<PeerProfile> getPeerById(@PathVariable Long id) {
        return ResponseEntity.ok(peerService.getPeerById(id));
    }

    /**
     * Requirement 4: Skill Want / Learn Directory (REST API)
     */
    @GetMapping("/skills/wanted")
    public ResponseEntity<List<WantedSkillGroupDto>> getWantedSkills(
            @RequestParam(value = "category", required = false) SkillCategory category) {
        return ResponseEntity.ok(skillService.getWantedSkillDirectory(category));
    }

    @GetMapping("/skills/offered")
    public ResponseEntity<List<Skill>> getOfferedSkills() {
        return ResponseEntity.ok(skillService.getAllOfferedSkills());
    }

    /**
     * Requirement 5: Search Skill Matches (REST API)
     */
    @GetMapping("/skills/search")
    public ResponseEntity<List<PeerProfile>> searchSkillMatches(
            @RequestParam("q") String query,
            @RequestParam(value = "category", required = false) SkillCategory category) {
        return ResponseEntity.ok(skillService.searchPeersTeachingSkill(query, category));
    }

    /**
     * Requirement 6: Book a session (REST API)
     */
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestParam("learnerId") Long learnerId,
                                           @Valid @RequestBody BookingRequestDto dto) {
        try {
            SessionBooking booking = bookingService.bookSession(learnerId, dto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Session successfully booked!");
            response.put("sessionId", booking.getSessionId());
            response.put("booking", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Requirement 6 & 8: Look up Session by unique Session ID
     */
    @GetMapping("/bookings/{sessionId}")
    public ResponseEntity<?> getBooking(@PathVariable String sessionId) {
        try {
            SessionBooking booking = bookingService.getBookingBySessionId(sessionId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Requirement 7: Booking History (REST API)
     */
    @GetMapping("/bookings/user/{userId}")
    public ResponseEntity<List<SessionBooking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getAllBookingsForUser(userId));
    }

    /**
     * Requirement 8: Cancel Session by Session ID (REST API)
     */
    @PostMapping("/bookings/{sessionId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable String sessionId,
                                           @RequestBody(required = false) CancelBookingDto cancelDto) {
        try {
            String reason = cancelDto != null ? cancelDto.getCancellationReason() : "Cancelled via API";
            SessionBooking cancelled = bookingService.cancelSession(sessionId, reason, null);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Session cancelled successfully");
            response.put("sessionId", cancelled.getSessionId());
            response.put("status", cancelled.getStatus());
            response.put("cancelledAt", cancelled.getCancelledAt());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
