package com.Project.Unsolved.dto;

import com.Project.Unsolved.entity.Problem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProblemResponseDto {

    private Long id;

    private String title;
    private String description;

    private String city;
    private String state;

    private List<String> tags;

    private String createdByName;

    private Problem.ProblemStatus status;

    // Already exists in your UI
    private long upvoteCount;

    // New
    private boolean saved;

    // New
    private boolean upvoted;

    // Useful later
    private String createdAt;
    private long solutionCount;
}
