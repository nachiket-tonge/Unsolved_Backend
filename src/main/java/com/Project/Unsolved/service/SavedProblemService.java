package com.Project.Unsolved.service;

import com.Project.Unsolved.dto.ProblemResponseDto;
import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.SavedProblem;
import com.Project.Unsolved.entity.User;
import com.Project.Unsolved.repository.ProblemRepository;
import com.Project.Unsolved.repository.SavedProblemRepository;
import com.Project.Unsolved.repository.UserRepository;
import com.Project.Unsolved.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavedProblemService {

    private final SavedProblemRepository savedProblemRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public SavedProblemService(
            SavedProblemRepository savedProblemRepository,
            ProblemRepository problemRepository,
            UserRepository userRepository,
            SecurityUtils securityUtils
    ) {
        this.savedProblemRepository = savedProblemRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    public void saveProblem(Long problemId) {

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow();

        Problem problem = problemRepository.findById(problemId).orElseThrow();

        if (savedProblemRepository.existsByUserAndProblem(user, problem)) {
            return;
        }

        SavedProblem savedProblem = SavedProblem.builder()
                .user(user)
                .problem(problem)
                .build();

        savedProblemRepository.save(savedProblem);
    }

    public void unsaveProblem(Long problemId) {

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow();

        Problem problem = problemRepository.findById(problemId).orElseThrow();

        savedProblemRepository.deleteByUserAndProblem(user, problem);
    }

    public List<ProblemResponseDto> getSavedProblems() {

        List<SavedProblem> savedProblems =
                savedProblemRepository.findByUser(
                        userRepository.findById(
                                securityUtils.getCurrentUserId()
                        ).orElseThrow()
                );

        List<ProblemResponseDto> list = new ArrayList<>();

        for (SavedProblem saved : savedProblems) {

            Problem problem = saved.getProblem();

            ProblemResponseDto dto = new ProblemResponseDto();

            dto.setId(problem.getId());
            dto.setTitle(problem.getTitle());
            dto.setDescription(problem.getDescription());
            dto.setCity(problem.getCity());
            dto.setState(problem.getState());
            dto.setTags(problem.getTags());
            dto.setStatus(problem.getStatus());
            dto.setCreatedByName(problem.getCreatedBy().getEmail());

            list.add(dto);
        }

        return list;
    }

}