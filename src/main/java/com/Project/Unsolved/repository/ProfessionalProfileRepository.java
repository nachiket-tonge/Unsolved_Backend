package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.BaseProfile;
import com.Project.Unsolved.entity.ProfessionalProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalProfileRepository extends JpaRepository<ProfessionalProfile,Long> {
    Optional<ProfessionalProfile> findByBaseProfile(BaseProfile baseProfile);
}
