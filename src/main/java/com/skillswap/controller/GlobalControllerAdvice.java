package com.skillswap.controller;

import com.skillswap.entity.PeerProfile;
import com.skillswap.entity.User;
import com.skillswap.entity.enums.SkillCategory;
import com.skillswap.service.PeerService;
import com.skillswap.service.UserService;
import com.skillswap.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;
    private final PeerService peerService;

    public GlobalControllerAdvice(UserService userService, PeerService peerService) {
        this.userService = userService;
        this.peerService = peerService;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(HttpSession session) {
        Long userId = SessionHelper.getLoggedInUserId(session);
        if (userId != null) {
            try {
                return userService.getById(userId);
            } catch (Exception e) {
                // If user deleted or invalid session
                SessionHelper.clear(session);
            }
        }
        return null;
    }

    @ModelAttribute("currentPeer")
    public PeerProfile getCurrentPeer(HttpSession session) {
        Long userId = SessionHelper.getLoggedInUserId(session);
        if (userId != null) {
            return peerService.getPeerByUserId(userId).orElse(null);
        }
        return null;
    }

    @ModelAttribute("allCategories")
    public SkillCategory[] getAllCategories() {
        return SkillCategory.values();
    }
}
