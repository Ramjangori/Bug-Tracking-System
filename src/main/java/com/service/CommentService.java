package com.service;

import java.util.List;

import com.dto.CommentRequest;
import com.dto.CommentResponse;

public interface CommentService {

    CommentResponse createComment(CommentRequest request);

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getAllComments();

    CommentResponse updateComment(Long id, CommentRequest request);

    void deleteComment(Long id);
}
