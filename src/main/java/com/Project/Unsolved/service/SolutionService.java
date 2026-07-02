package com.Project.Unsolved.service;

import com.Project.Unsolved.dto.CreateSolutionRequestDto;
import com.Project.Unsolved.dto.ProblemResponseDto;
import com.Project.Unsolved.dto.SolutionResponseDto;
import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.Role;
import com.Project.Unsolved.entity.Solution;
import com.Project.Unsolved.entity.User;
import com.Project.Unsolved.repository.ProblemRepository;
import com.Project.Unsolved.repository.SolutionRepository;
import com.Project.Unsolved.repository.UserRepository;
import com.Project.Unsolved.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolutionService {
    private final SolutionRepository solutionRepository ;
    private final SecurityUtils securityUtils ;
    private final UserRepository userRepository ;
    private final ProblemRepository problemRepository;

    SolutionService(SolutionRepository solutionRepository,SecurityUtils securityUtils,UserRepository userRepository,ProblemRepository problemRepository){
        this.solutionRepository = solutionRepository ;
        this.securityUtils = securityUtils ;
        this.userRepository = userRepository;
        this.problemRepository = problemRepository ;
    }

    //create solution
    public void createSolution(CreateSolutionRequestDto dto){

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow();
        Problem problem = problemRepository.findById(dto.getProblemId()).orElseThrow();

        // 🔒 Role check
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("Only students can submit solutions");
        }

        // ❌ prevent solving solved problem
        if (problem.getStatus() == Problem.ProblemStatus.SOLVED) {
            throw new RuntimeException("Problem already solved");
        }

        // 🔥 set IN_PROGRESS
        if (problem.getStatus() == Problem.ProblemStatus.OPEN) {
            problem.setStatus(Problem.ProblemStatus.IN_PROGRESS);
            problemRepository.save(problem);
        }

        Solution solution = Solution.builder()
                .demoUrl(dto.getDemoUrl())
                .repoUrl(dto.getRepoUrl())
                .summary(dto.getSummary())
                .user(user)
                .problem(problem)
                .build();

        solutionRepository.save(solution);
    }

    //accept solution
    public void acceptSolution(Long solutionId ,Long userId){

        User user = userRepository.findById(userId).orElseThrow();



        Solution solution  = solutionRepository.findById(solutionId).orElseThrow(() -> new RuntimeException("Solution Not Found"));
        Problem problem = solution.getProblem();
        if (!problem.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Only problem owner can accept solution");
        }

        boolean alreadyAccepted = solutionRepository
                .existsByProblemAndStatus(problem, Solution.SolutionStatus.ACCEPTED);

        if (alreadyAccepted) {
            throw new RuntimeException("Solution already accepted for this problem");
        }
        solution.setStatus(Solution.SolutionStatus.ACCEPTED);
        problem.setStatus(Problem.ProblemStatus.SOLVED);
        solutionRepository.save(solution);
        problemRepository.save(problem);

    }

    //get Solutions by Problem
    public List<SolutionResponseDto> getSolutionByProblem(Long problemId){
        Problem problem = problemRepository.findById(problemId).orElseThrow();
        List<Solution> solutions = solutionRepository.findByProblem(problem);
        List<SolutionResponseDto> solutionList = new ArrayList<>() ;
        solutions.sort((a, b) -> {
            if (a.getStatus() == Solution.SolutionStatus.ACCEPTED) return -1;
            if (b.getStatus() == Solution.SolutionStatus.ACCEPTED) return 1;
            return 0;
        });

        for(Solution solution : solutions){
            SolutionResponseDto dto = new SolutionResponseDto();
            dto.setId(solution.getId());
            dto.setDemoUrl(solution.getDemoUrl());
            dto.setStatus(solution.getStatus());
            dto.setSummary(solution.getSummary());
            dto.setRepoUrl(solution.getRepoUrl());
            dto.setProblemId(problem.getId());
            dto.setProblemTitle(problem.getTitle());
            dto.setCreatedAt(solution.getCreatedAt());
            if (solution.getUser().getBaseProfile() != null) {
                dto.setStudentName(
                        solution.getUser().getBaseProfile().getName()
                );
            }
            solutionList.add(dto);
        }
        return solutionList;
    }

    public int getMySubmissionCount() {

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow();

        return solutionRepository.findByUser(user).size();
    }
    public List<SolutionResponseDto> getMySolutions() {

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow();

        List<Solution> solutions = solutionRepository.findByUser(user);

        List<SolutionResponseDto> response = new ArrayList<>();

        for (Solution solution : solutions) {

            SolutionResponseDto dto = new SolutionResponseDto();

            dto.setId(solution.getId());

            dto.setProblemId(solution.getProblem().getId());
            dto.setProblemTitle(solution.getProblem().getTitle());

            dto.setSummary(solution.getSummary());
            dto.setRepoUrl(solution.getRepoUrl());
            dto.setDemoUrl(solution.getDemoUrl());
            dto.setCreatedAt(solution.getCreatedAt());

            dto.setStudentName(
                    solution.getUser()
                            .getBaseProfile()
                            .getName()
            );

            dto.setStatus(solution.getStatus());



            response.add(dto);
        }

        return response;
    }

}
