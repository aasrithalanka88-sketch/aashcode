package com.skillswap.service.impl;

import com.skillswap.dto.PeerRegistrationDto;
import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.entity.enums.SkillLevel;
import com.skillswap.entity.enums.SkillType;
import com.skillswap.repository.PeerProfileRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.PeerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeerServiceImpl implements PeerService {

    private final PeerProfileRepository peerProfileRepository;
    private final UserRepository userRepository;

    public PeerServiceImpl(PeerProfileRepository peerProfileRepository, UserRepository userRepository) {
        this.peerProfileRepository = peerProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PeerProfile registerOrUpdatePeer(Long userId, PeerRegistrationDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        PeerProfile profile = peerProfileRepository.findByUserId(userId)
                .orElse(new PeerProfile());

        profile.setUser(user);
        profile.setHeadline(dto.getHeadline().trim());
        profile.setAboutExperience(dto.getAboutExperience());
        profile.setExperienceYears(dto.getExperienceYears() != null ? dto.getExperienceYears() : 1);
        profile.setAvailability(dto.getAvailability() != null ? dto.getAvailability().trim() : "Flexible");
        profile.setLocation(dto.getLocation() != null ? dto.getLocation().trim() : "Remote / Online");
        profile.setActive(true);
        profile.setUpdatedAt(LocalDateTime.now());

        // Clear existing skills to rebuild with updated inputs
        profile.getSkills().clear();

        // 1. Process TEACHING skills
        if (dto.getTeachingSkills() != null && !dto.getTeachingSkills().isBlank()) {
            String[] teachTokens = dto.getTeachingSkills().split("[,;\\n]+");
            for (String rawToken : teachTokens) {
                String token = rawToken.trim();
                if (!token.isEmpty()) {
                    SkillCategory cat = dto.getTeachingCategory() != null ? dto.getTeachingCategory() : guessCategory(token);
                    SkillLevel lvl = dto.getTeachingLevel() != null ? dto.getTeachingLevel() : SkillLevel.ADVANCED;
                    Skill skill = new Skill(token, cat, SkillType.TEACH, lvl, dto.getTeachingDescription());
                    profile.addSkill(skill);
                }
            }
        }

        // 2. Process LEARNING skills
        if (dto.getLearningSkills() != null && !dto.getLearningSkills().isBlank()) {
            String[] learnTokens = dto.getLearningSkills().split("[,;\\n]+");
            for (String rawToken : learnTokens) {
                String token = rawToken.trim();
                if (!token.isEmpty()) {
                    SkillCategory cat = dto.getLearningCategory() != null ? dto.getLearningCategory() : guessCategory(token);
                    SkillLevel lvl = dto.getLearningLevel() != null ? dto.getLearningLevel() : SkillLevel.BEGINNER;
                    Skill skill = new Skill(token, cat, SkillType.LEARN, lvl, dto.getLearningDescription());
                    profile.addSkill(skill);
                }
            }
        }

        user.setPeerProfile(profile);
        return peerProfileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeerProfile> getAllActivePeers() {
        return peerProfileRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public PeerProfile getPeerById(Long id) {
        return peerProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Peer profile not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PeerProfile> getPeerByUserId(Long userId) {
        return peerProfileRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeerProfile> searchPeers(String query) {
        if (query == null || query.isBlank()) {
            return getAllActivePeers();
        }
        return peerProfileRepository.searchPeers(query.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeerProfile> findPeersTeachingSkill(String skillName) {
        if (skillName == null || skillName.isBlank()) {
            return getAllActivePeers();
        }
        return peerProfileRepository.findPeersTeachingSkill(skillName.trim());
    }

    private SkillCategory guessCategory(String skillName) {
        String lower = skillName.toLowerCase();
        if (lower.contains("java") || lower.contains("python") || lower.contains("c++") || lower.contains("react") ||
            lower.contains("angular") || lower.contains("vue") || lower.contains("spring") || lower.contains("sql") ||
            lower.contains("docker") || lower.contains("git") || lower.contains("aws") || lower.contains("html") ||
            lower.contains("css") || lower.contains("javascript") || lower.contains("typescript") || lower.contains("backend") ||
            lower.contains("frontend") || lower.contains("web") || lower.contains("api") || lower.contains("code")) {
            return SkillCategory.PROGRAMMING;
        } else if (lower.contains("design") || lower.contains("figma") || lower.contains("ui") || lower.contains("ux") ||
                   lower.contains("photoshop") || lower.contains("illustrator") || lower.contains("logo") || lower.contains("animation")) {
            return SkillCategory.DESIGN;
        } else if (lower.contains("spanish") || lower.contains("french") || lower.contains("german") || lower.contains("english") ||
                   lower.contains("japanese") || lower.contains("mandarin") || lower.contains("korean") || lower.contains("language")) {
            return SkillCategory.LANGUAGES;
        } else if (lower.contains("guitar") || lower.contains("piano") || lower.contains("music") || lower.contains("singing") ||
                   lower.contains("drums") || lower.contains("audio") || lower.contains("violin")) {
            return SkillCategory.MUSIC;
        } else if (lower.contains("machine learning") || lower.contains("ai") || lower.contains("data") || lower.contains("deep learning")) {
            return SkillCategory.DATA_SCIENCE;
        } else if (lower.contains("marketing") || lower.contains("seo") || lower.contains("sales") || lower.contains("content")) {
            return SkillCategory.MARKETING;
        } else if (lower.contains("business") || lower.contains("finance") || lower.contains("accounting") || lower.contains("management")) {
            return SkillCategory.BUSINESS;
        }
        return SkillCategory.OTHER;
    }
}
