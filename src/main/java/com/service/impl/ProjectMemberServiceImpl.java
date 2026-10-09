package com.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dto.UserResponse;
import com.entity.Project;
import com.entity.ProjectMember;
import com.entity.User;
import com.enums.Role;
import com.exception.ProjectNotFoundException;
import com.exception.UserNotFoundException;
import com.mapper.UserMapper;
import com.repository.ProjectMemberRepository;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import com.service.ProjectMemberService;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ProjectMemberServiceImpl(
            ProjectMemberRepository projectMemberRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            UserMapper userMapper) {

        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


  

    @Override
    public void addMember(Long projectId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project Not Found with Id : " + projectId));

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        // Manager can add member only to his own project
        if (currentUser.getRole() == Role.MANAGER) {

            if (!project.getCreatedBy().getId()
                    .equals(currentUser.getId())) {

                throw new AccessDeniedException(
                        "Manager can add members only to their own project.");
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found with Id : " + userId));

        boolean alreadyMember =
                projectMemberRepository.existsByProject_IdAndUser_Id(
                        projectId,
                        userId);

        if (alreadyMember) {

            throw new AccessDeniedException(
                    "User is already a member of this project.");
        }

        ProjectMember projectMember = new ProjectMember();

        projectMember.setProject(project);
        projectMember.setUser(user);

        projectMemberRepository.save(projectMember);
    }


   

    @Override
    public List<UserResponse> getAllMembers(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project Not Found with Id : " + projectId));

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        // Manager can see members only of his own project
        if (currentUser.getRole() == Role.MANAGER) {

            if (!project.getCreatedBy().getId()
                    .equals(currentUser.getId())) {

                throw new AccessDeniedException(
                        "Manager can access only their own project.");
            }
        }

        // Developer and Tester must be project members
        if (currentUser.getRole() == Role.DEVELOPER
                || currentUser.getRole() == Role.TESTER) {

            boolean isMember =
                    projectMemberRepository.existsByProject_IdAndUser_Id(
                            projectId,
                            currentUser.getId());

            if (!isMember) {

                throw new AccessDeniedException(
                        "You are not a member of this project.");
            }
        }

        List<ProjectMember> members =
                projectMemberRepository.findByProject_Id(projectId);

        List<UserResponse> users = new ArrayList<>();

        for (ProjectMember member : members) {

            users.add(userMapper.toResponse(member.getUser()));
        }

        return users;
    }


    

    @Override
    public UserResponse getMemberById(Long projectId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project Not Found with Id : " + projectId));

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        // Manager can see member only from his own project
        if (currentUser.getRole() == Role.MANAGER) {

            if (!project.getCreatedBy().getId()
                    .equals(currentUser.getId())) {

                throw new AccessDeniedException(
                        "Manager can access only their own project.");
            }
        }

        // Developer and Tester must be project members
        if (currentUser.getRole() == Role.DEVELOPER
                || currentUser.getRole() == Role.TESTER) {

            boolean isMember =
                    projectMemberRepository.existsByProject_IdAndUser_Id(
                            projectId,
                            currentUser.getId());

            if (!isMember) {

                throw new AccessDeniedException(
                        "You are not a member of this project.");
            }
        }

        ProjectMember member =
                projectMemberRepository
                        .findByProject_IdAndUser_Id(projectId, userId)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User is not a member of this project."));

        return userMapper.toResponse(member.getUser());
    }


  

    @Override
    public void deleteMember(Long projectId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project Not Found with Id : " + projectId));

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        // Manager can remove member only from his own project
        if (currentUser.getRole() == Role.MANAGER) {

            if (!project.getCreatedBy().getId()
                    .equals(currentUser.getId())) {

                throw new AccessDeniedException(
                        "Manager can remove members only from their own project.");
            }
        }

        ProjectMember member =
                projectMemberRepository
                        .findByProject_IdAndUser_Id(projectId, userId)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User is not a member of this project."));

        projectMemberRepository.delete(member);
    }
}