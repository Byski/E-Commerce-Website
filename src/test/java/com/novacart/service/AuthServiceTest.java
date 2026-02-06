package com.novacart.service;

import com.novacart.dto.AuthRequest;
import com.novacart.dto.RegisterRequest;
import com.novacart.exception.BadRequestException;
import com.novacart.model.Role;
import com.novacart.model.User;
import com.novacart.repository.UserRepository;
import com.novacart.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AuthServiceTest {
    @Test
    void registerCreatesUserAndReturnsToken() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        JwtService jwtService = Mockito.mock(JwtService.class);
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);

        Mockito.when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded");
        Mockito.when(jwtService.generateToken(any(User.class))).thenReturn("token");

        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService, authManager);

        RegisterRequest request = new RegisterRequest();
        setField(request, "email", "user@example.com");
        setField(request, "password", "password");

        String token = authService.register(request).getToken();
        assertEquals("token", token);

        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void registerFailsForDuplicateEmail() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        JwtService jwtService = Mockito.mock(JwtService.class);
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);

        Mockito.when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService, authManager);

        RegisterRequest request = new RegisterRequest();
        setField(request, "email", "user@example.com");
        setField(request, "password", "password");

        assertThrows(BadRequestException.class, () -> authService.register(request));
    }

    @Test
    void loginReturnsToken() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        JwtService jwtService = Mockito.mock(JwtService.class);
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);

        User user = new User("user@example.com", "encoded", Role.USER);
        Mockito.when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateToken(user)).thenReturn("token");

        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService, authManager);

        AuthRequest request = new AuthRequest();
        setField(request, "email", "user@example.com");
        setField(request, "password", "password");

        String token = authService.login(request).getToken();
        assertEquals("token", token);
    }

    private void setField(Object target, String field, String value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
