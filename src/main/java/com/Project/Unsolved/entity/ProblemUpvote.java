package com.Project.Unsolved.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "problem_upvotes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "problem_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemUpvote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
}
