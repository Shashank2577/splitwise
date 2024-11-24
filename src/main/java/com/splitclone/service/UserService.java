package com.splitclone.service;

import com.splitclone.dto.ProfileResponse;
import com.splitclone.dto.UpdateProfileRequest;
import com.splitclone.model.User;
import com.splitclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return mapToProfileResponse(user);
    }

    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (StringUtils.hasText(request.getName())) {
            user.setName(request.getName());
        }

        if (StringUtils.hasText(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (StringUtils.hasText(request.getFcmToken())) {
            user.setFcmToken(request.getFcmToken());
        }

        user = userRepository.save(user);
        return mapToProfileResponse(user);
    }

    private ProfileResponse mapToProfileResponse(User user) {
        ProfileResponse response = new ProfileResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setFcmToken(user.getFcmToken());
        response.setProfileImageUrl(user.getProfileImageUrl());
        return response;
    }
}
