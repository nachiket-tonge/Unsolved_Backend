package com.Project.Unsolved.service;

import com.Project.Unsolved.dto.CommentResponseDto;
import com.Project.Unsolved.dto.CreateProblemRequestDto;
import com.Project.Unsolved.dto.ProblemResponseDto;
import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.ProblemComment;
import com.Project.Unsolved.entity.ProblemUpvote;
import com.Project.Unsolved.entity.User;
import com.Project.Unsolved.repository.ProblemCommentRepository;
import com.Project.Unsolved.repository.ProblemRepository;
import com.Project.Unsolved.repository.ProblemUpvoteRepository;
import com.Project.Unsolved.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final ProblemUpvoteRepository problemUpvoteRepository;
    private final ProblemCommentRepository problemCommentRepository;

    public ProblemService(
            UserRepository userRepository,
            ProblemRepository problemRepository,
            ProblemUpvoteRepository problemUpvoteRepository,
            ProblemCommentRepository problemCommentRepository
            ) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.problemUpvoteRepository = problemUpvoteRepository ;
        this.problemCommentRepository = problemCommentRepository;
    }

    public void createProblem(Long userId, CreateProblemRequestDto dto){
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

    public List<ProblemResponseDto> getAllProblems() {

        List<Problem> problems = problemRepository.findAll();

        List<ProblemResponseDto> responseList = new ArrayList<>();

        for (Problem problem : problems) {

            ProblemResponseDto dto = new ProblemResponseDto();

            dto.setId(problem.getId());
            dto.setTitle(problem.getTitle());
            dto.setDescription(problem.getDescription());
            dto.setCity(problem.getCity());
            dto.setState(problem.getState());
            dto.setTags(problem.getTags());
            dto.setStatus(problem.getStatus());

            // important
            dto.setCreatedByName(problem.getCreatedBy().getEmail());
            // or name if you store it in BaseProfile

            responseList.add(dto);
        }

        return responseList;
    }
    public ProblemResponseDto getProblemById(Long problemId) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow();

        ProblemResponseDto dto = new ProblemResponseDto();

        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setCity(problem.getCity());
        dto.setState(problem.getState());
        dto.setTags(problem.getTags());
        dto.setStatus(problem.getStatus());
        dto.setCreatedByName(problem.getCreatedBy().getEmail());

        return dto;
    }

    public void toggleUpvote(Long problemId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow();
        Problem problem = problemRepository.findById(problemId).orElseThrow();

        boolean exists = problemUpvoteRepository.existsByUserAndProblem(user, problem);

        if (exists) {
            problemUpvoteRepository.deleteByUserAndProblem(user, problem); // remove upvote
        } else {
            ProblemUpvote upvote = ProblemUpvote.builder()
                    .user(user)
                    .problem(problem)
                    .build();

            problemUpvoteRepository.save(upvote);
        }
    }

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

    public List<CommentResponseDto> getComments(Long problemId) {

        Problem problem = problemRepository.findById(problemId).orElseThrow();

        List<ProblemComment> comments =
                problemCommentRepository.findByProblemOrderByCreatedAtDesc(problem);

        List<CommentResponseDto> list = new ArrayList<>();

        for (ProblemComment c : comments) {
            CommentResponseDto dto = new CommentResponseDto();

            dto.setContent(c.getContent());
            dto.setUserName(
                    c.getUser().getBaseProfile().getName()
            );
            dto.setCreatedAt(c.getCreatedAt());

            list.add(dto);
        }

        return list;
    }
}
