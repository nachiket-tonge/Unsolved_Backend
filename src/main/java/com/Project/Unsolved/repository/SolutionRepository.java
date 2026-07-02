package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.Solution;
import com.Project.Unsolved.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {

    boolean existsByProblemAndStatus(
            Problem problem,
            Solution.SolutionStatus status
    );

    List<Solution> findByProblem(Problem problem);

    List<Solution> findByUser(User user);

    long countByUser(User user);
    long countByProblemCreatedByAndStatus(
            User user,
            Solution.SolutionStatus status
    );
    long countByProblem(Problem problem);
}
