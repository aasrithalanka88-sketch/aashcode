package com.skillswap.dto;

import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.enums.SkillCategory;
import java.util.ArrayList;
import java.util.List;

public class WantedSkillGroupDto {

    private String skillName;
    private SkillCategory category;
    private List<PeerProfile> interestedPeers = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();

    public WantedSkillGroupDto() {
    }

    public WantedSkillGroupDto(String skillName, SkillCategory category) {
        this.skillName = skillName;
        this.category = category;
    }

    public void addPeer(PeerProfile peer, String description) {
        if (!interestedPeers.contains(peer)) {
            this.interestedPeers.add(peer);
        }
        if (description != null && !description.isBlank() && !descriptions.contains(description)) {
            this.descriptions.add(description);
        }
    }

    public int getLearnerCount() {
        return interestedPeers.size();
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public List<PeerProfile> getInterestedPeers() {
        return interestedPeers;
    }

    public void setInterestedPeers(List<PeerProfile> interestedPeers) {
        this.interestedPeers = interestedPeers;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }
}
