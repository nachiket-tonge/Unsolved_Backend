package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem,Long> {

    long countByCreatedBy(User user);

    long countByCreatedByAndStatus(
            User user,
            Problem.ProblemStatus status
    );
    List<Problem> findByCreatedBy(User user);
}
