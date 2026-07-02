package com.Project.Unsolved.controller;

import com.Project.Unsolved.dto.ProblemResponseDto;
import com.Project.Unsolved.service.SavedProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved")
public class SavedProblemController {

    private final SavedProblemService savedProblemService;

    public SavedProblemController(
            SavedProblemService savedProblemService
    ) {
        this.savedProblemService = savedProblemService;
    }

    @PostMapping("/{problemId}")
    public ResponseEntity<String> saveProblem(
            @PathVariable Long problemId
    ) {

        savedProblemService.saveProblem(problemId);

        return ResponseEntity.ok("Problem saved successfully");
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<String> unsaveProblem(
            @PathVariable Long problemId
    ) {

        savedProblemService.unsaveProblem(problemId);

        return ResponseEntity.ok("Problem removed from saved list");
    }

    @GetMapping
    public ResponseEntity<List<ProblemResponseDto>> getSavedProblems() {

        return ResponseEntity.ok(
                savedProblemService.getSavedProblems()
        );
    }
}