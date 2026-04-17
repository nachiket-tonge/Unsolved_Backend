package com.Project.Unsolved.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "professional_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Example values:
     * Teacher, Doctor, Agriculturist, Engineer, etc.
     */
    @Column(nullable = false)
    private String professionType;

    @OneToOne(optional = false)
    @JoinColumn(name = "base_profile_id", unique = true)
    private BaseProfile baseProfile;
}
