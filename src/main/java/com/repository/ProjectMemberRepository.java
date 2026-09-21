package com.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.ProjectMember;

public interface ProjectMemberRepository
        extends JpaRepository<ProjectMember, Long> {

    boolean existsByProject_IdAndUser_Id(Long projectId, Long userId);
    List<ProjectMember> findByUser_Id(Long userId);
    List<ProjectMember> findByProject_Id(Long projectId);
    Optional<ProjectMember> findByProject_IdAndUser_Id(Long projectId, Long userId);
    
}