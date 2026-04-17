package com.Project.Unsolved.service;

import com.Project.Unsolved.dto.CompleteProfessionalProfileRequestDto;
import com.Project.Unsolved.dto.CompleteStudentProfileRequestDto;
import com.Project.Unsolved.dto.UserProfileResponseDto;
import com.Project.Unsolved.entity.*;
import com.Project.Unsolved.repository.BaseProfileRepository;
import com.Project.Unsolved.repository.ProfessionalProfileRepository;
import com.Project.Unsolved.repository.StudentProfileRepository;
import com.Project.Unsolved.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;
    private final UserRepository userRepository;
    private final BaseProfileRepository baseProfileRepository;

    public ProfileService(
            StudentProfileRepository studentProfileRepository,
            ProfessionalProfileRepository professionalProfileRepository,
            BaseProfileRepository baseProfileRepository,
            UserRepository userRepository
    ) {
        this.studentProfileRepository = studentProfileRepository;
        this.professionalProfileRepository = professionalProfileRepository;
        this.baseProfileRepository = baseProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Completes student profile: Updates BaseProfile + creates StudentProfile
     */
    // ✅ EXISTING: Profile completion methods (keep as-is, just add role setting)
    public void completeStudentProfile(Long userId, CompleteStudentProfileRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();

        BaseProfile baseProfile = baseProfileRepository.findByUser(user).orElseThrow();

        // Update base profile
        baseProfile.setCity(requestDto.getCity());
        baseProfile.setState(requestDto.getState());
        baseProfile.setBio(requestDto.getBio());
        baseProfile.setProfileImageUrl(requestDto.getProfileImageUrl());
        baseProfileRepository.save(baseProfile);

        // Create/update student profile
        StudentProfile studentProfile = studentProfileRepository.findByBaseProfile(baseProfile)
                .orElse(new StudentProfile());
        studentProfile.setBaseProfile(baseProfile);
        studentProfile.setDegree("B.Tech");
        studentProfile.setSkills(requestDto.getSkills());
        studentProfileRepository.save(studentProfile);
    }

    /**
     * Completes professional profile: Updates BaseProfile + creates ProfessionalProfile
     */
    public void completeProfessionalProfile(Long userId, CompleteProfessionalProfileRequestDto requestDto) {
        // 1. Fetch authenticated user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));


        // 3. Fetch user's BaseProfile
        BaseProfile baseProfile = baseProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Base profile not found for user"));

        // 4. Update BaseProfile ✅ FIXED: Now saving!
        baseProfile.setCity(requestDto.getCity());
        baseProfile.setState(requestDto.getState());
        baseProfile.setProfileImageUrl(requestDto.getProfileImageUrl());
        baseProfile.setBio(requestDto.getBio());
        baseProfileRepository.save(baseProfile);  // ✅ FIXED: Save BaseProfile changes

        // 5. Get or create ProfessionalProfile
        ProfessionalProfile professionalProfile = professionalProfileRepository.findByBaseProfile(baseProfile)
                .orElse(new ProfessionalProfile());

        // 6. Set professional-specific data
        professionalProfile.setBaseProfile(baseProfile);
        professionalProfile.setProfessionType(requestDto.getProfessionType());

        // 7. Save
        professionalProfileRepository.save(professionalProfile);
    }

    // 🔥 NEW: Get complete user profile for frontend UI
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        BaseProfile baseProfile = baseProfileRepository.findByUser(user).orElseThrow();

        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setId(user.getId());
        dto.setName(baseProfile.getName());
        dto.setEmail(user.getEmail());
        dto.setCity(baseProfile.getCity());
        dto.setState(baseProfile.getState());
        dto.setBio(baseProfile.getBio());
        dto.setPhoto(baseProfile.getProfileImageUrl());
        dto.setRole(user.getRole());

        // Add role-specific data
        if (Role.STUDENT.equals(user.getRole())) {
            StudentProfile student = studentProfileRepository.findByBaseProfile(baseProfile).orElse(null);
            if (student != null) {
                dto.setDegree(student.getDegree());
                dto.setSkills(student.getSkills());
            }
        } else if (Role.PROFESSIONAL.equals(user.getRole())) {
            ProfessionalProfile prof = professionalProfileRepository.findByBaseProfile(baseProfile).orElse(null);
            if (prof != null) {
                dto.setProfession(prof.getProfessionType());
            }
        }

        return dto;
    }
}