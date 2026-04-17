package com.Project.Unsolved.controller;

import com.Project.Unsolved.dto.CompleteProfessionalProfileRequestDto;
import com.Project.Unsolved.dto.CompleteStudentProfileRequestDto;
import com.Project.Unsolved.dto.UserProfileResponseDto;
import com.Project.Unsolved.repository.UserRepository;
import com.Project.Unsolved.security.SecurityUtils;
import com.Project.Unsolved.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final SecurityUtils securityUtils;

    public ProfileController(ProfileService profileService, SecurityUtils securityUtils) {
        this.profileService = profileService;
        this.securityUtils = securityUtils;
    }

    // ✅ EXISTING: Profile completion
    @PostMapping("/student")
    public ResponseEntity<String> completeStudentProfile(@RequestBody CompleteStudentProfileRequestDto requestDto) {
        Long userId = securityUtils.getCurrentUserId();
        profileService.completeStudentProfile(userId, requestDto);
        return ResponseEntity.ok("Profile saved successfully");
    }

    @PostMapping("/professional")
    public ResponseEntity<String> completeProfessionalProfile(@RequestBody CompleteProfessionalProfileRequestDto requestDto) {
        Long userId = securityUtils.getCurrentUserId();
        profileService.completeProfessionalProfile(userId, requestDto);
        return ResponseEntity.ok("Profile saved successfully");
    }

    // 🔥 NEW: Get current user profile for UI display
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getCurrentUserProfile() {
        Long userId = securityUtils.getCurrentUserId();
        UserProfileResponseDto profile = profileService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }
}
