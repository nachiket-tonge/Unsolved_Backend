package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.SavedProblem;
import com.Project.Unsolved.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedProblemRepository
        extends JpaRepository<SavedProblem, Long> {

    boolean existsByUserAndProblem(User user, Problem problem);

    List<SavedProblem> findByUser(User user);

    void deleteByUserAndProblem(User user, Problem problem);
    long countByUser(User user);
}