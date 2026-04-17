package com.Project.Unsolved.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String degree; // default: B.Tech

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "student_skills",
            joinColumns = @JoinColumn(name = "student_profile_id")
    )
    @Column(name = "skill", nullable = false)
    private Set<String> skills;

    @OneToOne(optional = false)
    @JoinColumn(name = "base_profile_id", unique = true)
    private BaseProfile baseProfile;
}
