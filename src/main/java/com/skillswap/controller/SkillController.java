package com.skillswap.controller;

import com.skillswap.dto.WantedSkillGroupDto;
import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.service.PeerService;
import com.skillswap.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/skills")
public class SkillController {

    private final SkillService skillService;
    private final PeerService peerService;

    public SkillController(SkillService skillService, PeerService peerService) {
        this.skillService = skillService;
        this.peerService = peerService;
    }

    /**
     * Requirement 4: Skill Want/Learn Directory
     * Clearly displays skills that peers want to learn and associates them with respective peers.
     */
    @GetMapping("/wanted")
    public String viewWantedSkillDirectory(@RequestParam(value = "category", required = false) SkillCategory category,
                                          Model model) {
        List<WantedSkillGroupDto> wantedSkillGroups = skillService.getWantedSkillDirectory(category);
        model.addAttribute("wantedSkillGroups", wantedSkillGroups);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("totalWantedSkills", wantedSkillGroups.size());
        return "skill/directory";
    }

    /**
     * Requirement 5: Search Skill Matches
     * Search for a specific skill you want to learn and instantly find matching peers who teach it.
     */
    @GetMapping("/search")
    public String searchSkillMatches(@RequestParam(value = "q", required = false) String query,
                                     @RequestParam(value = "category", required = false) SkillCategory category,
                                     Model model) {
        List<PeerProfile> matchingPeers = skillService.searchPeersTeachingSkill(query, category);

        model.addAttribute("query", query);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("matchingPeers", matchingPeers);
        model.addAttribute("matchCount", matchingPeers.size());
        model.addAttribute("popularSkills", skillService.getPopularOfferedSkillNames());

        return "skill/search";
    }
}
