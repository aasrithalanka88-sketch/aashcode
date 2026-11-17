package com.skillswap.controller;

import com.skillswap.dto.PeerRegistrationDto;
import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.enums.SkillCategory;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/peers")
public class PeerController {

    private final PeerService peerService;
    private final UserService userService;

    public PeerController(PeerService peerService, UserService userService) {
        this.peerService = peerService;
        this.userService = userService;
    }

    /**
     * Requirement 3: View All Peers
     * Lists active peers along with their offered (teach) and desired (learn) skills.
     */
    @GetMapping
    public String listAllPeers(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "category", required = false) SkillCategory category,
                               Model model) {
        List<PeerProfile> peers;
        if (search != null && !search.isBlank()) {
            peers = peerService.searchPeers(search.trim());
        } else {
            peers = peerService.getAllActivePeers();
        }

        if (category != null) {
            peers = peers.stream()
                    .filter(p -> p.getSkills().stream().anyMatch(s -> s.getCategory() == category))
                    .collect(Collectors.toList());
        }

        model.addAttribute("peers", peers);
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedCategory", category);
        return "peer/list";
    }

    /**
     * Individual Peer Profile view
     */
    @GetMapping("/{id}")
    public String viewPeerProfile(@PathVariable Long id, Model model) {
        PeerProfile peer = peerService.getPeerById(id);
        model.addAttribute("peer", peer);
        return "peer/profile";
    }

    /**
     * Requirement 2: Register as a Peer (Form)
     */
    @GetMapping("/register")
    public String showRegisterAsPeerForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = SessionHelper.getLoggedInUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in first to register as a Peer.");
            return "redirect:/login";
        }

        User user = userService.getById(userId);
        PeerRegistrationDto dto = new PeerRegistrationDto();

        // If user already has a peer profile, prefill the form for editing
        Optional<PeerProfile> existingOpt = peerService.getPeerByUserId(userId);
        if (existingOpt.isPresent()) {
            PeerProfile existing = existingOpt.get();
            dto.setHeadline(existing.getHeadline());
            dto.setAboutExperience(existing.getAboutExperience());
            dto.setExperienceYears(existing.getExperienceYears());
            dto.setAvailability(existing.getAvailability());
            dto.setLocation(existing.getLocation());

            String teachingStr = existing.getTeachingSkills().stream()
                    .map(Skill::getName)
                    .collect(Collectors.joining(", "));
            dto.setTeachingSkills(teachingStr);

            String learningStr = existing.getLearningSkills().stream()
                    .map(Skill::getName)
                    .collect(Collectors.joining(", "));
            dto.setLearningSkills(learningStr);
        }

        model.addAttribute("peerDto", dto);
        model.addAttribute("isEditing", existingOpt.isPresent());
        return "peer/register";
    }

    /**
     * Requirement 2: Register as a Peer (Submission)
     */
    @PostMapping("/register")
    public String processRegisterAsPeer(@Valid @ModelAttribute("peerDto") PeerRegistrationDto peerDto,
                                        BindingResult bindingResult,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        Long userId = SessionHelper.getLoggedInUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session expired. Please log in again.");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "peer/register";
        }

        try {
            PeerProfile savedProfile = peerService.registerOrUpdatePeer(userId, peerDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Congratulations! Your Peer Profile has been successfully saved. Other peers can now discover and book sessions with you!");
            return "redirect:/peers/" + savedProfile.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving peer profile: " + e.getMessage());
            return "redirect:/peers/register";
        }
    }
}
