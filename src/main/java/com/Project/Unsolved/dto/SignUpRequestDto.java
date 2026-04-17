package com.Project.Unsolved.dto;

import com.Project.Unsolved.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpRequestDto {

    @NotNull(message = "Name is required")
    private String name;

    @Email(message = "Invalid Email Format")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is Required")
    @Size(min = 8,message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role shall not be empty")
    private Role role;
}
