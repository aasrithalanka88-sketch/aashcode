package com.skillswap.service;

import com.skillswap.dto.UserRegisterDto;
import com.skillswap.entity.User;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void registerStoresEncodedPassword() {
        UserRegisterDto dto = new UserRegisterDto("Test User", "test@example.com", "secret", null);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.register(dto);

        assertEquals("encoded-secret", user.getPassword());
    }

    @Test
    void authenticateRejectsMissingPassword() {
        assertTrue(userService.authenticate("test@example.com", null).isEmpty());
        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void authenticateChecksEncodedPassword() {
        User user = new User("Test User", "test@example.com", "encoded-secret", null);
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "encoded-secret")).thenReturn(true);

        assertTrue(userService.authenticate("test@example.com", "secret").isPresent());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUpgradesLegacyPlainTextPassword() {
        User user = new User("Test User", "test@example.com", "legacy-secret", null);
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("legacy-secret", "legacy-secret")).thenReturn(false);
        when(passwordEncoder.encode("legacy-secret")).thenReturn("encoded-secret");
        when(userRepository.save(user)).thenReturn(user);

        assertTrue(userService.authenticate("test@example.com", "legacy-secret").isPresent());
        assertEquals("encoded-secret", user.getPassword());
    }
}
