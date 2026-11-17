package com.skillswap.controller;

import com.skillswap.dto.UserLoginDto;
import com.skillswap.dto.UserRegisterDto;
import com.skillswap.entity.User;
import com.skillswap.service.UserService;
import com.skillswap.util.SessionHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        if (SessionHelper.isLoggedIn(session)) {
            return "redirect:/peers";
        }
        if (!model.containsAttribute("loginDto")) {
            model.addAttribute("loginDto", new UserLoginDto());
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginDto") UserLoginDto loginDto,
                               BindingResult bindingResult,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        Optional<User> userOpt = userService.authenticate(loginDto.getEmail(), loginDto.getPassword());
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password. Please try again.");
            return "redirect:/login";
        }

        User user = userOpt.get();
        SessionHelper.setLoggedInUser(session, user);
        redirectAttributes.addFlashAttribute("successMessage", "Welcome back, " + user.getName() + "!");
        return "redirect:/peers";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model, HttpSession session) {
        if (SessionHelper.isLoggedIn(session)) {
            return "redirect:/peers";
        }
        if (!model.containsAttribute("registerDto")) {
            model.addAttribute("registerDto", new UserRegisterDto());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("registerDto") UserRegisterDto registerDto,
                                  BindingResult bindingResult,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            User newUser = userService.register(registerDto);
            SessionHelper.setLoggedInUser(session, newUser);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Account created successfully! You are now logged in as " + newUser.getName() + ". Complete your Peer profile to start swapping skills!");
            return "redirect:/peers/register";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.registerDto", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        SessionHelper.clear(session);
        redirectAttributes.addFlashAttribute("infoMessage", "You have been logged out successfully.");
        return "redirect:/login";
    }

    /**
     * Quick demo login endpoint allowing 1-click login as seeded users for testing.
     */
    @GetMapping("/demo-login/{userId}")
    public String demoLogin(@PathVariable Long userId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getById(userId);
            SessionHelper.setLoggedInUser(session, user);
            redirectAttributes.addFlashAttribute("successMessage", "Logged in as demo user: " + user.getName());
            return "redirect:/peers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found: " + e.getMessage());
            return "redirect:/login";
        }
    }
}
