package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Bug;
import com.entity.Project;
import com.entity.User;
import com.enums.BugPriority;
import com.enums.BugStatus;

public interface BugRepository extends JpaRepository<Bug,Long> {

	List<Bug> findByStatus(BugStatus status);

	List<Bug> findByPriority(BugPriority priority);

	List<Bug> findByProject(Project project);

	List<Bug> findByAssignedTo(User user);

	List<Bug> findByAssignedToId(Long userId);

	List<Bug> findByCreatedById(Long userId);
}


