package com.magnus.testapis.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magnus.testapis.dto.UserResponse;
import com.magnus.testapis.entity.User;
import com.magnus.testapis.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all users
     */
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get user by ID
     */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        return convertToUserResponse(user);
    }
    
    /**
     * Convert User entity to UserResponse DTO
     */
    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt()
        );
    }
}