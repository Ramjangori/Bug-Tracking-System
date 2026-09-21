package com.service.impl;

import com.dto.BugRequest;
import com.dto.BugResponse;
import com.entity.Bug;
import com.entity.Project;
import com.entity.User;
import com.enums.Role;
import com.exception.BugNotFoundException;
import com.exception.ProjectNotFoundException;
import com.exception.UserNotFoundException;
import com.mapper.BugMapper;
import com.repository.BugRepository;
import com.repository.ProjectMemberRepository;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import com.service.BugService;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugServiceImpl implements BugService {

    private final BugMapper bugMapper;
    private final BugRepository bugRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public BugServiceImpl(
            BugMapper bugMapper,
            BugRepository bugRepository,
            UserRepository userRepository,
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository) {

        this.bugMapper = bugMapper;
        this.bugRepository = bugRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }


    @Override
    public BugResponse assignBug(Long bugId, Long developerId) {

        // 1. Bug find 
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() ->
                        new BugNotFoundException(
                                "Bug Not Found with Id : " + bugId));


        // 2. Developer find 
        User developer = userRepository.findById(developerId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found with Id : " + developerId));


        // 3. User DEVELOPER hona chahiye
        if (developer.getRole() != Role.DEVELOPER) {

            throw new AccessDeniedException(
                    "Bug can only be assigned to a developer.");
        }


        // 4. Developer is project ka member hai ya nahi
        boolean isProjectMember =
                projectMemberRepository.existsByProject_IdAndUser_Id(
                        bug.getProject().getId(),
                        developerId
                );


        if (!isProjectMember) {

            throw new AccessDeniedException(
                    "Developer is not a member of this project.");
        }


        // 5. Developer ko bug assign 
        bug.setAssignedTo(developer);


        // 6. Save
        Bug updatedBug = bugRepository.save(bug);


        // 7. Response
        return bugMapper.toResponse(updatedBug);
    }
    // Create Bug
    @Override
    public BugResponse createBug(BugRequest request) {

        // Find project
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project with id " + request.getProjectId()
                                        + " not found."));


        // Get current logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User createdBy = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));


        // Create Bug entity
        Bug bug = bugMapper.toEntity(request);

        bug.setProject(project);
        bug.setCreatedBy(createdBy);
        bug.setAssignedTo(null);

        // Save
        Bug savedBug = bugRepository.save(bug);

        return bugMapper.toResponse(savedBug);
    }


    // Get Bug By ID
    @Override
    public BugResponse getBugById(Long id) {

        Bug bug = bugRepository.findById(id)
                .orElseThrow(() ->
                        new BugNotFoundException(
                                "Bug Not Found with Id : " + id));

        return bugMapper.toResponse(bug);
    }


    // Get All Bugs
    @Override
    public List<BugResponse> getAllBugs() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));


        List<Bug> bugs;

        if (user.getRole() == Role.ADMIN ||
                user.getRole() == Role.MANAGER) {

            // ADMIN + MANAGER → All bugs
            bugs = bugRepository.findAll();

        } else if (user.getRole() == Role.DEVELOPER) {

            // DEVELOPER → Only assigned bugs
            bugs = bugRepository.findByAssignedToId(user.getId());

        } else if (user.getRole() == Role.TESTER) {

            // TESTER → Only bugs created by himself
            bugs = bugRepository.findByCreatedById(user.getId());

        } else {

            bugs = List.of();
        }


        return bugs.stream()
                .map(bugMapper::toResponse)
                .toList();
    }


    // Update Bug
    @Override
    public BugResponse updateBug(Long id, BugRequest request) {

        Bug existingBug = bugRepository.findById(id)
                .orElseThrow(() ->
                        new BugNotFoundException(
                                "Bug Not Found with Id : " + id));


        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String currentEmail = authentication.getName();


        boolean isCreator =
                currentEmail.equals(existingBug.getCreatedBy().getEmail());


        boolean isAssignedDeveloper =
                existingBug.getAssignedTo() != null &&
                        currentEmail.equals(existingBug.getAssignedTo().getEmail());


        // Only creator OR assigned developer can update
        if (!isCreator && !isAssignedDeveloper) {

            throw new AccessDeniedException(
                    "You can update only your own or assigned bug.");
        }


        existingBug.setTitle(request.getTitle());
        existingBug.setDescription(request.getDescription());
        existingBug.setPriority(request.getPriority());
        existingBug.setStatus(request.getStatus());


        Bug updatedBug = bugRepository.save(existingBug);

        return bugMapper.toResponse(updatedBug);
    }


    // Delete Bug
    @Override
    public void deleteBug(Long id) {

        Bug bug = bugRepository.findById(id)
                .orElseThrow(() ->
                        new BugNotFoundException(
                                "Bug Not Found with Id : " + id));

        bugRepository.delete(bug);
    }
}