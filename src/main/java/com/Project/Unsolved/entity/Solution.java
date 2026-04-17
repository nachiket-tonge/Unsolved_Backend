package com.Project.Unsolved.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "problem_id")
    private Problem problem;


    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(length = 2000)
    private String summary;

    private String repoUrl;
    private String demoUrl;


    public enum SolutionStatus {
        SUBMITTED,
        ACCEPTED,
        REJECTED
    }

    @Enumerated(EnumType.STRING)
    private SolutionStatus status;


    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = SolutionStatus.SUBMITTED;
    }
}
