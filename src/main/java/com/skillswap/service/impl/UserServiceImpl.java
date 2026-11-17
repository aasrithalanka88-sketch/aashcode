package com.skillswap.service.impl;

import com.skillswap.dto.UserRegisterDto;
import com.skillswap.entity.User;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(UserRegisterDto dto) {
        String cleanEmail = dto.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException("An account with email " + cleanEmail + " already exists.");
        }
        User user = new User();
        user.setName(dto.getName().trim());
        user.setEmail(cleanEmail);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBio(dto.getBio());
        user.setPhone(dto.getPhone());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> authenticate(String email, String password) {
        if (email == null || password == null) return Optional.empty();
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
            // Upgrade passwords written by older versions of the application.
            if (password.equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(password));
                return Optional.of(userRepository.save(user));
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return userRepository.findByEmailIgnoreCase(email.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null) return false;
        return userRepository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    public User updateProfile(Long userId, String name, String bio, String phone) {
        User user = getById(userId);
        if (name != null && !name.isBlank()) user.setName(name.trim());
        if (bio != null) user.setBio(bio.trim());
        if (phone != null) user.setPhone(phone.trim());
        return userRepository.save(user);
    }
}
