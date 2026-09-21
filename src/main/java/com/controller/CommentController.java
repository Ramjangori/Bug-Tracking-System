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

import com.dto.CommentRequest;
import com.dto.CommentResponse;
import com.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Create Comment
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest request) {

        CommentResponse response = commentService.createComment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get Comment By Id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(
            @PathVariable Long id) {

        CommentResponse response = commentService.getCommentById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Get All Comments
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {

        List<CommentResponse> response = commentService.getAllComments();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Update Comment
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse response =
                commentService.updateComment(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // Delete Comment
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long id) {

        commentService.deleteComment(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Comment deleted successfully.");
    }
}