package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem,Long> {
}
