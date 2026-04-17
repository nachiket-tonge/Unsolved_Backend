package com.Project.Unsolved.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CompleteStudentProfileRequestDto {

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotEmpty
    private Set<String> skills;

    private String bio;
    private String profileImageUrl;
}
