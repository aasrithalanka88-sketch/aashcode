package com.skillswap.repository;

import com.skillswap.entity.Skill;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.entity.enums.SkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByType(SkillType type);

    List<Skill> findByTypeAndCategory(SkillType type, SkillCategory category);

    List<Skill> findByNameContainingIgnoreCaseAndType(String name, SkillType type);

    @Query("SELECT s FROM Skill s " +
           "JOIN FETCH s.peerProfile p " +
           "JOIN FETCH p.user u " +
           "WHERE s.type = :type " +
           "ORDER BY s.name ASC")
    List<Skill> findAllWithTypeAndPeers(@Param("type") SkillType type);

    @Query("SELECT DISTINCT s.name FROM Skill s WHERE s.type = :type ORDER BY s.name ASC")
    List<String> findDistinctSkillNamesByType(@Param("type") SkillType type);
}
