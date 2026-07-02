package com.Project.Unsolved.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class PlatformStatsDto {

    private long totalProblems;
    private long totalSolutions;
    private long totalUsers;
    private long activeTeams;

}
