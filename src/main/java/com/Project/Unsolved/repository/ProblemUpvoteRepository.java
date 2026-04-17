package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.ProblemUpvote;
import com.Project.Unsolved.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemUpvoteRepository extends JpaRepository<ProblemUpvote, Long> {

    boolean existsByUserAndProblem(User user, Problem problem);

    long countByProblem(Problem problem);

    void deleteByUserAndProblem(User user, Problem problem);
}