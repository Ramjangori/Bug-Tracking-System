package com.service.impl;

import com.dto.UserResponse;
import com.entity.Project;
import com.entity.ProjectMember;
import com.entity.User;
import com.exception.ProjectNotFoundException;
import com.exception.UserNotFoundException;
import com.mapper.UserMapper;
import com.repository.ProjectMemberRepository;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import com.service.ProjectMemberService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found with Id : " + userId));

        boolean alreadyMember =
                projectMemberRepository.existsByProject_IdAndUser_Id(
                        projectId,
                        userId
                );

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

        // Check project exists
        projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project Not Found with Id : " + projectId));

        List<ProjectMember> members =
                projectMemberRepository.findByProject_Id(projectId);

        return members.stream()
                .map(ProjectMember::getUser)
                .map(userMapper::toResponse)
                .toList();
    }


    @Override
    public UserResponse getMemberById(Long projectId, Long userId) {

        // Check project exists
        projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project Not Found with Id : " + projectId));

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

        ProjectMember member =
                projectMemberRepository
                        .findByProject_IdAndUser_Id(projectId, userId)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User is not a member of this project."));

        projectMemberRepository.delete(member);
    }
}