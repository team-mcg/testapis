package com.magnus.testapis.controller;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-api")
public class HealthController {
    
    @Value("${spring.application.name:jwt-auth-api}")
    private String appName;
    
    @Value("${spring.profiles.active:default}")
    private String activeProfile;
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("application", appName);
        healthInfo.put("profile", activeProfile);
        healthInfo.put("timestamp", LocalDateTime.now().toString());
        healthInfo.put("javaVersion", System.getProperty("java.version"));
        healthInfo.put("message", "JWT Authentication API is running");
        return healthInfo;
    }
    
    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "JWT Authentication API");
        info.put("version", "1.0.0");
        info.put("description", "Spring Boot 3.5.9 JWT Authentication Service");
        info.put("status", "Operational");
        return info;
    }
}