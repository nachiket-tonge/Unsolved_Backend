package com.Project.Unsolved.service;

import com.Project.Unsolved.dto.DashboardStatsDto;
import com.Project.Unsolved.dto.PlatformStatsDto;
import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.Solution;
import com.Project.Unsolved.entity.User;
import com.Project.Unsolved.repository.ProblemRepository;
import com.Project.Unsolved.repository.SavedProblemRepository;
import com.Project.Unsolved.repository.SolutionRepository;
import com.Project.Unsolved.repository.UserRepository;
import com.Project.Unsolved.security.SecurityUtils;
import org.springframework.stereotype.Service;
import com.Project.Unsolved.entity.Role;

@Service
public class DashboardService {

    private final ProblemRepository problemRepository;
    private final SavedProblemRepository savedProblemRepository;
    private final SolutionRepository solutionRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public DashboardService(
            ProblemRepository problemRepository,
            SavedProblemRepository savedProblemRepository,
            SolutionRepository solutionRepository,
            UserRepository userRepository,
            SecurityUtils securityUtils
    ) {
        this.problemRepository = problemRepository;
        this.savedProblemRepository = savedProblemRepository;
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    public DashboardStatsDto getDashboardStats() {

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow();

        DashboardStatsDto dto = new DashboardStatsDto();

        dto.setReportedProblems(
                problemRepository.countByCreatedBy(user)
        );

        dto.setSavedProblems(
                savedProblemRepository.countByUser(user)
        );

        if (user.getRole() == Role.STUDENT) {

            dto.setSubmittedSolutions(
                    solutionRepository.countByUser(user)
            );

        } else {

            dto.setAcceptedSolutions(
                    solutionRepository.countByProblemCreatedByAndStatus(
                            user,
                            Solution.SolutionStatus.ACCEPTED
                    )
            );

            dto.setPendingProblems(
                    problemRepository.countByCreatedByAndStatus(
                            user,
                            Problem.ProblemStatus.IN_PROGRESS
                    )
                            +
                            problemRepository.countByCreatedByAndStatus(
                                    user,
                                    Problem.ProblemStatus.OPEN
                            )
            );

        }

        return dto;
    }
    public PlatformStatsDto getPlatformStats() {

        return new PlatformStatsDto(
                problemRepository.count(),
                solutionRepository.count(),
                userRepository.count(),
                0L
        );

    }
}