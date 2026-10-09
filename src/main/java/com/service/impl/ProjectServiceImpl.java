package com.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dto.ProjectRequest;
import com.dto.ProjectResponse;
import com.entity.Project;
import com.entity.ProjectMember;
import com.entity.User;
import com.enums.Role;
import com.exception.ProjectNotFoundException;
import com.exception.UpdateProjectException;
import com.exception.UserNotFoundException;
import com.mapper.ProjectMapper;
import com.repository.ProjectMemberRepository;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import com.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ProjectMapper projectMapper,
            ProjectMemberRepository projectMemberRepository) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
        this.projectMemberRepository = projectMemberRepository;
    }


    @Override
    public ProjectResponse createProject(ProjectRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        Project project = projectMapper.toEntity(request);

        project.setCreatedBy(user);

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponse(savedProject);
    }


    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException("Project Not Found.."));

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        // ADMIN can see any project
        if (user.getRole() == Role.ADMIN) {

            return projectMapper.toResponse(project);
        }

        // MANAGER can see only his own project
        if (user.getRole() == Role.MANAGER) {

            if (!project.getCreatedBy().getId().equals(user.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own projects.");
            }

            return projectMapper.toResponse(project);
        }

        // DEVELOPER and TESTER must be project members
        boolean isMember = projectMemberRepository
                .existsByProject_IdAndUser_Id(id, user.getId());

        if (!isMember) {

            throw new AccessDeniedException(
                    "You are not a member of this project.");
        }

        return projectMapper.toResponse(project);
    }
    @Override
    public List<ProjectResponse> getAllProjects() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        // ADMIN can see all projects
        if (user.getRole() == Role.ADMIN) {

            List<Project> projects = projectRepository.findAll();

            return projectMapper.toResponse(projects);
        }

        // MANAGER can see only his own projects
        if (user.getRole() == Role.MANAGER) {

            List<Project> projects =
                    projectRepository.findByCreatedBy(user);

            return projectMapper.toResponse(projects);
        }

        // DEVELOPER and TESTER can see only their member projects
        List<ProjectMember> memberships =
                projectMemberRepository.findByUser_Id(user.getId());

        List<Project> projects = new ArrayList<>();

        for (ProjectMember member : memberships) {

            projects.add(member.getProject());
        }

        return projectMapper.toResponse(projects);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException("Project Not Found.."));

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        // Manager can update only his own project
        if (user.getRole() == Role.MANAGER
                && !project.getCreatedBy().getId().equals(user.getId())) {

            throw new UpdateProjectException(
                    "Manager can update only their own project.");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());

        Project updated = projectRepository.save(project);

        return projectMapper.toResponse(updated);
    }


    @Override
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException("Project Not Found.."));

        projectRepository.deleteById(id);
    }
}