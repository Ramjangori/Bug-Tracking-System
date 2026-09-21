package com.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.BugRequest;
import com.dto.BugResponse;
import com.service.BugService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bugs")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    // Create
    @PreAuthorize("hasAnyRole('TESTER','ADMIN')")
    @PostMapping
    public ResponseEntity<BugResponse> createBug(
            @Valid @RequestBody BugRequest request) {

        BugResponse response = bugService.createBug(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{bugId}/assign/{developerId}")
    public ResponseEntity<BugResponse> assignBug(
            @PathVariable Long bugId,
            @PathVariable Long developerId) {

        BugResponse response =
                bugService.assignBug(bugId, developerId);

        return ResponseEntity.ok(response);
    }

    // Get Bug By Id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<BugResponse> getBugById(
            @PathVariable Long id) {

        BugResponse response = bugService.getBugById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Get All Bugs
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<BugResponse>> getAllBugs() {

        List<BugResponse> response = bugService.getAllBugs();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Update Bug
    @PreAuthorize("hasAnyRole('DEVELOPER','TESTER')")
    @PutMapping("/{id}")
    public ResponseEntity<BugResponse> updateBug(
            @PathVariable Long id,
            @Valid @RequestBody BugRequest request) {

        BugResponse response = bugService.updateBug(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Delete Bug
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBug(
            @PathVariable Long id) {

        bugService.deleteBug(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Bug deleted successfully.");
    }
}