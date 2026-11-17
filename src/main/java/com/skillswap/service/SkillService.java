package com.skillswap.service;

import com.skillswap.dto.WantedSkillGroupDto;
import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.Skill;
import com.skillswap.entity.enums.SkillCategory;
import java.util.List;

public interface SkillService {
    List<WantedSkillGroupDto> getWantedSkillDirectory(SkillCategory category);
    List<PeerProfile> searchPeersTeachingSkill(String skillName, SkillCategory category);
    List<Skill> getAllWantedSkills();
    List<Skill> getAllOfferedSkills();
    List<String> getPopularOfferedSkillNames();
}
