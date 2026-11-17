package com.skillswap.repository;

import com.skillswap.entity.PeerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeerProfileRepository extends JpaRepository<PeerProfile, Long> {

    Optional<PeerProfile> findByUserId(Long userId);

    List<PeerProfile> findByActiveTrue();

    @Query("SELECT DISTINCT p FROM PeerProfile p " +
           "JOIN p.skills s " +
           "WHERE p.active = true " +
           "AND s.type = com.skillswap.entity.enums.SkillType.TEACH " +
           "AND LOWER(s.name) LIKE LOWER(CONCAT('%', :skillName, '%'))")
    List<PeerProfile> findPeersTeachingSkill(@Param("skillName") String skillName);

    @Query("SELECT DISTINCT p FROM PeerProfile p " +
           "JOIN p.skills s " +
           "WHERE p.active = true " +
           "AND (LOWER(p.user.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "     OR LOWER(p.headline) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "     OR LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<PeerProfile> searchPeers(@Param("query") String query);
}
