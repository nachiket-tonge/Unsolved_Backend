package com.Project.Unsolved.controller;

import com.Project.Unsolved.dto.CommentResponseDto;
import com.Project.Unsolved.dto.CreateProblemRequestDto;
import com.Project.Unsolved.dto.ProblemResponseDto;
import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.User;
import com.Project.Unsolved.repository.ProblemRepository;
import com.Project.Unsolved.security.SecurityUtils;
import com.Project.Unsolved.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;
    private final SecurityUtils securityUtils;

    public ProblemController(ProblemService problemService, SecurityUtils securityUtils) {
        this.problemService = problemService;
        this.securityUtils = securityUtils;
    }

    //creating problem
    @PostMapping()
    private ResponseEntity<String> createProblem(
            @RequestBody CreateProblemRequestDto dto
    ){
            Long userId = securityUtils.getCurrentUserId();
            problemService.createProblem(userId,dto);
            return ResponseEntity.ok("Problem Created");
    }

    //getting problem by id
    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponseDto> getProblemById(@PathVariable Long problemId) {
        return ResponseEntity.ok(problemService.getProblemById(problemId));
    }

    //fetching all problems
    @GetMapping
    public ResponseEntity<List<ProblemResponseDto>> getAllProblems() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    @PostMapping("/{problemId}/upvote")
    public ResponseEntity<String> toggleUpvote(@PathVariable Long problemId) {

        Long userId = securityUtils.getCurrentUserId();

        problemService.toggleUpvote(problemId, userId);

        return ResponseEntity.ok("Upvote toggled");
    }


    @PostMapping("/{problemId}/comment")
    public ResponseEntity<String> addComment(
            @PathVariable Long problemId,
            @RequestBody String content
    ) {
        Long userId = securityUtils.getCurrentUserId();
        problemService.addComment(problemId, userId, content);

        return ResponseEntity.ok("Comment added");
    }

    @GetMapping("/{problemId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable Long problemId
    ) {
        return ResponseEntity.ok(problemService.getComments(problemId));
    }
    @GetMapping("/me")
    public ResponseEntity<List<ProblemResponseDto>> getMyProblems() {

        return ResponseEntity.ok(
                problemService.getMyProblems()
        );

    }
}
