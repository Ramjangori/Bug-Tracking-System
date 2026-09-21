package com.repository;

import com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByCreatedBy(User u);
}
