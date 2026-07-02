package com.Project.Unsolved.controller;

import com.Project.Unsolved.dto.CreateSolutionRequestDto;
import com.Project.Unsolved.dto.SolutionResponseDto;
import com.Project.Unsolved.security.SecurityUtils;
import com.Project.Unsolved.service.SolutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solutions")
public class SolutionController {

    private final SolutionService solutionService;
    private final SecurityUtils securityUtils;

    public SolutionController(SolutionService solutionService, SecurityUtils securityUtils) {
        this.solutionService = solutionService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<String> submitSolution(@RequestBody CreateSolutionRequestDto dto){
        solutionService.createSolution(dto);
        return ResponseEntity.ok("Solution Submitted Succesfully");
    }

    @PostMapping("/{solutionId}/accept")
    public ResponseEntity<String> acceptSolution(@PathVariable Long solutionId){
        Long userId = securityUtils.getCurrentUserId();
        solutionService.acceptSolution(solutionId, userId);
        return ResponseEntity.ok("Solution Accepted Successfully");
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<SolutionResponseDto>> getSolutions(
            @PathVariable Long problemId
    ) {
        return ResponseEntity.ok(solutionService.getSolutionByProblem(problemId));
    }
    @GetMapping("/me/count")
    public ResponseEntity<Integer> getMySubmissionCount() {
        return ResponseEntity.ok(solutionService.getMySubmissionCount());
    }
    @GetMapping("/me")
    public ResponseEntity<List<SolutionResponseDto>> getMySolutions() {

        return ResponseEntity.ok(
                solutionService.getMySolutions()
        );

    }
}