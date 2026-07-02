package com.Project.Unsolved.service;

import com.Project.Unsolved.dto.CommentResponseDto;
import com.Project.Unsolved.dto.CreateProblemRequestDto;
import com.Project.Unsolved.dto.ProblemResponseDto;
import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.ProblemComment;
import com.Project.Unsolved.entity.ProblemUpvote;
import com.Project.Unsolved.entity.User;
import com.Project.Unsolved.repository.*;
import com.Project.Unsolved.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final ProblemUpvoteRepository problemUpvoteRepository;
    private final ProblemCommentRepository problemCommentRepository;
    private final SavedProblemRepository savedProblemRepository;
    private final SecurityUtils securityUtils;
    private final SolutionRepository solutionRepository;

    public ProblemService(
            UserRepository userRepository,
            ProblemRepository problemRepository,
            ProblemUpvoteRepository problemUpvoteRepository,
            ProblemCommentRepository problemCommentRepository,
            SavedProblemRepository savedProblemRepository,
            SecurityUtils securityUtils,
            SolutionRepository solutionRepository
    ) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.problemUpvoteRepository = problemUpvoteRepository;
        this.problemCommentRepository = problemCommentRepository;
        this.savedProblemRepository = savedProblemRepository;
        this.securityUtils = securityUtils;
        this.solutionRepository = solutionRepository ;
    }

    // Create Problem
    public void createProblem(Long userId, CreateProblemRequestDto dto) {

        User user = userRepository.findById(userId).orElseThrow();

        Problem problem = Problem.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .city(dto.getCity())
                .state(dto.getState())
                .tags(dto.getTags())
                .createdBy(user)
                .build();

        problemRepository.save(problem);
    }

    // Get All Problems
    public List<ProblemResponseDto> getAllProblems() {

        List<Problem> problems = problemRepository.findAll();
        List<ProblemResponseDto> responseList = new ArrayList<>();

        User currentUser = null;

        try {
            Long userId = securityUtils.getCurrentUserId();
            currentUser = userRepository.findById(userId).orElse(null);
        } catch (Exception ignored) {
            // Guest user
        }

        for (Problem problem : problems) {

            boolean saved = false;

            if (currentUser != null) {
                saved = savedProblemRepository.existsByUserAndProblem(
                        currentUser,
                        problem
                );
            }

            responseList.add(mapToDto(problem, saved));
        }

        return responseList;
    }

    // Get Problem By Id
    public ProblemResponseDto getProblemById(Long problemId) {

        Problem problem = problemRepository
                .findById(problemId)
                .orElseThrow();

        boolean saved = false;

        try {

            Long userId = securityUtils.getCurrentUserId();

            User currentUser =
                    userRepository.findById(userId).orElse(null);

            if (currentUser != null) {

                saved = savedProblemRepository.existsByUserAndProblem(
                        currentUser,
                        problem
                );

            }

        } catch (Exception ignored) {
            // Guest user
        }

        return mapToDto(problem, saved);
    }
    public List<ProblemResponseDto> getMyProblems() {

        Long userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow();

        List<Problem> problems =
                problemRepository.findByCreatedBy(user);

        List<ProblemResponseDto> list =
                new ArrayList<>();

        for (Problem problem : problems) {

            boolean saved =
                    savedProblemRepository.existsByUserAndProblem(
                            user,
                            problem
                    );

            list.add(
                    mapToDto(problem, saved)
            );

        }

        return list;
    }

    // Toggle Upvote
    public void toggleUpvote(Long problemId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow();
        Problem problem = problemRepository.findById(problemId).orElseThrow();

        boolean exists =
                problemUpvoteRepository.existsByUserAndProblem(user, problem);

        if (exists) {

            problemUpvoteRepository.deleteByUserAndProblem(user, problem);

        } else {

            ProblemUpvote upvote = ProblemUpvote.builder()
                    .user(user)
                    .problem(problem)
                    .build();

            problemUpvoteRepository.save(upvote);
        }
    }

    // Add Comment
    public void addComment(Long problemId, Long userId, String content) {

        User user = userRepository.findById(userId).orElseThrow();
        Problem problem = problemRepository.findById(problemId).orElseThrow();

        ProblemComment comment = ProblemComment.builder()
                .content(content)
                .user(user)
                .problem(problem)
                .build();

        problemCommentRepository.save(comment);
    }

    // Get Comments
    public List<CommentResponseDto> getComments(Long problemId) {

        Problem problem = problemRepository.findById(problemId).orElseThrow();

        List<ProblemComment> comments =
                problemCommentRepository.findByProblemOrderByCreatedAtDesc(problem);

        List<CommentResponseDto> list = new ArrayList<>();

        for (ProblemComment comment : comments) {

            CommentResponseDto dto = new CommentResponseDto();

            dto.setContent(comment.getContent());

            if (comment.getUser().getBaseProfile() != null) {
                dto.setUserName(
                        comment.getUser().getBaseProfile().getName()
                );
            } else {
                dto.setUserName(comment.getUser().getEmail());
            }

            dto.setCreatedAt(comment.getCreatedAt());

            list.add(dto);
        }

        return list;
    }

    // Common DTO Mapping
    private ProblemResponseDto mapToDto(
            Problem problem,
            boolean saved
    ) {

        ProblemResponseDto dto = new ProblemResponseDto();

        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setCity(problem.getCity());
        dto.setState(problem.getState());
        dto.setTags(problem.getTags());
        dto.setStatus(problem.getStatus());

        if (problem.getCreatedBy().getBaseProfile() != null) {
            dto.setCreatedByName(
                    problem.getCreatedBy().getBaseProfile().getName()
            );
        } else {
            dto.setCreatedByName(
                    problem.getCreatedBy().getEmail()
            );
        }
        dto.setSolutionCount(
                solutionRepository.countByProblem(problem)
        );

        dto.setSaved(saved);

        return dto;
    }
}