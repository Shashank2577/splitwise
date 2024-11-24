package com.splitclone.dto;

import lombok.Data;

@Data
public class ProfileResponse {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String profileImageUrl;
    private String fcmToken;
}
