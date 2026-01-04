package com.magnus.testapis.dto;


import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    
    private String username;
    private String email;
    private String role;
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String message;
    private boolean success;
    private Map<String, String> errors;  // For validation errors
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String username, String email, String role, String accessToken, 
                       Long expiresIn, String message, boolean success) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.message = message;
        this.success = success;
    }
    
    // Static factory methods for success/failure
    public static AuthResponse success(String username, String email, String role, 
                                      String accessToken, Long expiresIn, String message) {
        return new AuthResponse(username, email, role, accessToken, expiresIn, message, true);
    }
    
    public static AuthResponse failure(String message) {
        AuthResponse response = new AuthResponse();
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }
    
 // Factory method for validation errors
    public static AuthResponse validationError(Map<String, String> errors) {
        AuthResponse response = new AuthResponse();
        response.success = false;
        response.message = "Validation failed";
        response.errors = errors;
        return response;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public Long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }
}