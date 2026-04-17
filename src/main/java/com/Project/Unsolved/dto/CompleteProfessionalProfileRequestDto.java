package com.Project.Unsolved.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteProfessionalProfileRequestDto {

    @NotBlank(message = "Profession type is required")
    private String professionType;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    private String bio;

    private String profileImageUrl;
}
