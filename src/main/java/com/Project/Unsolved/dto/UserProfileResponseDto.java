package com.Project.Unsolved.dto;

import com.Project.Unsolved.entity.Role;
import lombok.Data;


import java.util.Set;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String state;
    private String bio;
    private String photo;
    private Role role ;
    private String degree;      // Student only
    private Set<String> skills; // Student only
    private String profession;  // Professional only
    private boolean profileCompleted;
}
