package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.BaseProfile;
import com.Project.Unsolved.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile,Long> {
    Optional<StudentProfile> findByBaseProfile(BaseProfile baseProfile);
}
