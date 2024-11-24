package com.splitclone.controller;

import com.splitclone.dto.ProfileResponse;
import com.splitclone.dto.UpdateProfileRequest;
import com.splitclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserProfile(authentication.getName()));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @ModelAttribute UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(authentication.getName(), request));
    }
}
