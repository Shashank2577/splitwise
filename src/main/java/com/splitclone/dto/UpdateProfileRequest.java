package com.splitclone.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phoneNumber;
    private String fcmToken;
}
