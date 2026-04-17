package com.Project.Unsolved.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProblemRequestDto {

    private String title;
    private String description;
    private String city;
    private String state;
    private List<String> tags;
}