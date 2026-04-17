package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.Problem;
import com.Project.Unsolved.entity.ProblemComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemCommentRepository extends JpaRepository<ProblemComment, Long> {

    List<ProblemComment> findByProblemOrderByCreatedAtDesc(Problem problem);
}
