package com.Project.Unsolved.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequestDto {
    @NotNull
    @Email(message = "Email is not valid")
    private String email;

    @NotNull
    @Size(min = 8,message = "Password Must be atleast 8 characters")
    private String password ;
}
