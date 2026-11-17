package com.skillswap.service;

import com.skillswap.dto.UserRegisterDto;
import com.skillswap.entity.User;
import java.util.Optional;

public interface UserService {
    User register(UserRegisterDto dto);
    Optional<User> authenticate(String email, String password);
    User getById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User updateProfile(Long userId, String name, String bio, String phone);
}
