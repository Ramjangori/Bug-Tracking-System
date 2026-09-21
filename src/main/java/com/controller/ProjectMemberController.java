package com.controller;

import com.dto.UserResponse;
import com.service.ProjectMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }


    // Add member to project
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{projectId}/members/{userId}")
    public ResponseEntity<String> addMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectMemberService.addMember(projectId, userId);

        return ResponseEntity.ok(
                "User added to project successfully."
        );
    }


    // Get all members of a project
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<UserResponse>> getAllMembers(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                projectMemberService.getAllMembers(projectId)
        );
    }


    // Get specific member of a project
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{projectId}/members/{userId}")
    public ResponseEntity<UserResponse> getMemberById(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                projectMemberService.getMemberById(projectId, userId)
        );
    }


    // Remove member from project
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<String> deleteMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectMemberService.deleteMember(projectId, userId);

        return ResponseEntity.ok(
                "User removed from project successfully."
        );
    }
}