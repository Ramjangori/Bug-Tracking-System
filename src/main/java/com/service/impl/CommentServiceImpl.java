package com.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.exception.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dto.CommentRequest;
import com.dto.CommentResponse;
import com.entity.Bug;
import com.entity.Comment;
import com.entity.User;
import com.mapper.CommentMapper;
import com.repository.BugRepository;
import com.repository.CommentRepository;
import com.repository.UserRepository;
import com.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BugRepository bugRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            UserRepository userRepository,
            BugRepository bugRepository,
            CommentMapper commentMapper) {

        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.bugRepository = bugRepository;
        this.commentMapper = commentMapper;
    }

    // Create Comment
    @Override
    public CommentResponse createComment(CommentRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with id " + request.getUserId() + " not found."));

        Bug bug = bugRepository.findById(request.getBugId())
                .orElseThrow(() ->
                        new BugNotFoundException(
                                "Bug with id " + request.getBugId() + " not found."));

        Comment comment = commentMapper.toEntity(request);

        comment.setUser(user);
        comment.setBug(bug);

        LocalDateTime now = LocalDateTime.now();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponse(savedComment);
    }

    // Get Comment By Id
    @Override
    public CommentResponse getCommentById(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new CommentNotFoundException(
                                "Comment with id " + id + " not found."));

        return commentMapper.toResponse(comment);
    }

    // Get All Comments
    @Override
    public List<CommentResponse> getAllComments() {

        List<Comment> comments = commentRepository.findAll();

        return commentMapper.toResponse(comments);
    }

    // Update Comment
    @Override
    public CommentResponse updateComment(Long id, CommentRequest request) {

        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new CommentNotFoundException(
                                "Comment with id " + id + " not found."));

        // check authorization
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String commentEmail = existingComment.getUser().getEmail();

        if(!email.equals(commentEmail)){

            throw new UpdateCommentException("You Can Update  Only Your Comment..");
        }



        existingComment.setMessage(request.getMessage());
        existingComment.setUpdatedAt(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(existingComment);

        return commentMapper.toResponse(updatedComment);
    }

    // Delete Comment
    @Override
    public void deleteComment(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new CommentNotFoundException(
                                "Comment with id " + id + " not found."));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String commentEmail = comment.getUser().getEmail();

        if(!email.equals(commentEmail)){

             throw new DeleteCommentException("You Can Delete  Only Your Comment..");
        }

        commentRepository.delete(comment);
    }
}