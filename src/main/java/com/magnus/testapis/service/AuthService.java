package com.magnus.testapis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.magnus.testapis.config.JwtService;
import com.magnus.testapis.dto.AuthResponse;
import com.magnus.testapis.dto.LoginRequest;
import com.magnus.testapis.dto.RegisterRequest;
import com.magnus.testapis.dto.UserProfileResponse;
import com.magnus.testapis.entity.User;
import com.magnus.testapis.repository.UserRepository;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.failure("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.failure("Email already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        
        userRepository.save(user);
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername());
        
        return AuthResponse.success(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                token,
                jwtService.getExpirationTime(),
                "Registration successful"
        );
    }
    
    public AuthResponse login(LoginRequest request) {
        try {
            // First, find user by email to get their username
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Then authenticate using the actual username
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),  // Use username from database
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT token
            String token = jwtService.generateToken(user.getUsername());
            
            return AuthResponse.success(
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    token,
                    jwtService.getExpirationTime(),
                    "Login successful"
            );
            
        } catch (BadCredentialsException e) {
            return AuthResponse.failure("Invalid password");
        } catch (Exception e) {
            return AuthResponse.failure("Invalid email or password");
        }
    }
    
    public AuthResponse validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtService.validateToken(token, userDetails)) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                
                return AuthResponse.success(
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        token,
                        jwtService.getExpirationTime(),
                        "Token is valid"
                );
            } else {
                return AuthResponse.failure("Invalid token");
            }
        } catch (Exception e) {
            return AuthResponse.failure("Invalid token: " + e.getMessage());
        }
    }

    public UserProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username).get();
        
        return mapToProfileResponse(user);
    }
    
    private UserProfileResponse mapToProfileResponse(User user) {
        return UserProfileResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                
                .build();
    }
}