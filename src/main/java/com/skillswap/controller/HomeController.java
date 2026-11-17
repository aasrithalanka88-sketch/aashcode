package com.skillswap.controller;

import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.Skill;
import com.skillswap.service.BookingService;
import com.skillswap.service.PeerService;
import com.skillswap.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final PeerService peerService;
    private final SkillService skillService;
    private final BookingService bookingService;

    public HomeController(PeerService peerService, SkillService skillService, BookingService bookingService) {
        this.peerService = peerService;
        this.skillService = skillService;
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<PeerProfile> activePeers = peerService.getAllActivePeers();
        List<Skill> offeredSkills = skillService.getAllOfferedSkills();
        List<Skill> wantedSkills = skillService.getAllWantedSkills();

        model.addAttribute("featuredPeers", activePeers.stream().limit(6).toList());
        model.addAttribute("popularSkillNames", skillService.getPopularOfferedSkillNames().stream().limit(8).toList());
        model.addAttribute("totalPeers", activePeers.size());
        model.addAttribute("totalOfferedSkills", offeredSkills.size());
        model.addAttribute("totalWantedSkills", wantedSkills.size());
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());

        return "home";
    }
}
