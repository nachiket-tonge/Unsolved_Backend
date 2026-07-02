package com.Project.Unsolved.dto;

import com.Project.Unsolved.entity.Solution;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolutionResponseDto {

    private Long id;

    private Long problemId;
    private String problemTitle;

    private String summary;
    private String repoUrl;
    private String demoUrl;

    private String studentName;

    private Solution.SolutionStatus status;
    private LocalDateTime createdAt;
}