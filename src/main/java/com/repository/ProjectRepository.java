package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Project;
import com.entity.User;
import com.enums.Role;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByCreatedBy(User u);
    List<Project> findByCreatedBy(User user);
   
}
