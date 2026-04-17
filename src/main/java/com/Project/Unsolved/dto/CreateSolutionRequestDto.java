package com.Project.Unsolved.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSolutionRequestDto {

    private Long problemId;
    private String summary;
    private String repoUrl;
    private String demoUrl;
}
