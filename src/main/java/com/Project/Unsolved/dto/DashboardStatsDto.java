package com.Project.Unsolved.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardStatsDto {

    private long reportedProblems;
    private long savedProblems;

    // Student
    private long submittedSolutions;

    // Professional
    private long acceptedSolutions;
    private long pendingProblems;
}