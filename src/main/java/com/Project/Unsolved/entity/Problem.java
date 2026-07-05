package com.Project.Unsolved.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String city;
    private String state;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "problem_tags",
            joinColumns = @JoinColumn(name = "problem_id")
    )
    @Column(name = "tag")
    @OrderColumn(name = "tag_index")
    private List<String> tags = new ArrayList<>();

    public enum ProblemStatus {
        OPEN,
        IN_PROGRESS,
        SOLVED
    }

    @Enumerated(EnumType.STRING)
    private ProblemStatus status;


    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = ProblemStatus.OPEN;
    }
}
