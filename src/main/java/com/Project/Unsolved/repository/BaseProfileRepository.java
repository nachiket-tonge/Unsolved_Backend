package com.Project.Unsolved.repository;

import com.Project.Unsolved.entity.BaseProfile;
import com.Project.Unsolved.entity.ProfessionalProfile;
import com.Project.Unsolved.entity.StudentProfile;
import com.Project.Unsolved.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseProfileRepository extends JpaRepository<BaseProfile,Long> {
    Optional<BaseProfile> findByUser(User user);



}
