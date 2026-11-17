package com.skillswap.service;

import com.skillswap.dto.PeerRegistrationDto;
import com.skillswap.entity.PeerProfile;
import java.util.List;
import java.util.Optional;

public interface PeerService {
    PeerProfile registerOrUpdatePeer(Long userId, PeerRegistrationDto dto);
    List<PeerProfile> getAllActivePeers();
    PeerProfile getPeerById(Long id);
    Optional<PeerProfile> getPeerByUserId(Long userId);
    List<PeerProfile> searchPeers(String query);
    List<PeerProfile> findPeersTeachingSkill(String skillName);
}
