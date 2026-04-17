package com.Project.Unsolved.dto;

import com.Project.Unsolved.entity.Solution;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SolutionResponseDto {

    private Long id;
    private String summary;
    private String repoUrl;
    private String demoUrl;
    private String studentName;
    private Solution.SolutionStatus status;
}
