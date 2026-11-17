package com.skillswap.service.impl;

import com.skillswap.dto.WantedSkillGroupDto;
import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.Skill;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.entity.enums.SkillType;
import com.skillswap.repository.PeerProfileRepository;
import com.skillswap.repository.SkillRepository;
import com.skillswap.service.SkillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final PeerProfileRepository peerProfileRepository;

    public SkillServiceImpl(SkillRepository skillRepository, PeerProfileRepository peerProfileRepository) {
        this.skillRepository = skillRepository;
        this.peerProfileRepository = peerProfileRepository;
    }

    @Override
    public List<WantedSkillGroupDto> getWantedSkillDirectory(SkillCategory category) {
        List<Skill> wantedSkills = skillRepository.findAllWithTypeAndPeers(SkillType.LEARN);

        Map<String, WantedSkillGroupDto> groupMap = new LinkedHashMap<>();

        for (Skill s : wantedSkills) {
            if (category != null && s.getCategory() != category) {
                continue;
            }
            String key = s.getName().trim().toLowerCase();
            WantedSkillGroupDto group = groupMap.computeIfAbsent(key, k -> {
                WantedSkillGroupDto g = new WantedSkillGroupDto();
                g.setSkillName(s.getName().trim());
                g.setCategory(s.getCategory());
                return g;
            });

            if (s.getPeerProfile() != null) {
                group.addPeer(s.getPeerProfile(), s.getDescription());
            }
        }

        List<WantedSkillGroupDto> result = new ArrayList<>(groupMap.values());
        // Sort by number of peers wanting the skill descending, then by name
        result.sort((a, b) -> {
            int cmp = Integer.compare(b.getLearnerCount(), a.getLearnerCount());
            return cmp != 0 ? cmp : a.getSkillName().compareToIgnoreCase(b.getSkillName());
        });

        return result;
    }

    @Override
    public List<PeerProfile> searchPeersTeachingSkill(String skillName, SkillCategory category) {
        if (skillName == null || skillName.isBlank()) {
            List<PeerProfile> all = peerProfileRepository.findByActiveTrue();
            if (category == null) {
                return all;
            }
            return all.stream()
                    .filter(p -> p.getTeachingSkills().stream().anyMatch(s -> s.getCategory() == category))
                    .collect(Collectors.toList());
        }

        String search = skillName.trim().toLowerCase();
        List<PeerProfile> matching = peerProfileRepository.findPeersTeachingSkill(search);

        if (category != null) {
            matching = matching.stream()
                    .filter(p -> p.getTeachingSkills().stream().anyMatch(s ->
                            s.getCategory() == category && s.getName().toLowerCase().contains(search)))
                    .collect(Collectors.toList());
        }

        return matching;
    }

    @Override
    public List<Skill> getAllWantedSkills() {
        return skillRepository.findByType(SkillType.LEARN);
    }

    @Override
    public List<Skill> getAllOfferedSkills() {
        return skillRepository.findByType(SkillType.TEACH);
    }

    @Override
    public List<String> getPopularOfferedSkillNames() {
        return skillRepository.findDistinctSkillNamesByType(SkillType.TEACH);
    }
}
