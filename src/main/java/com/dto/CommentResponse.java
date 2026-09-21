package com.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;

    private String message;

    private Long userId;

    private String userName;

    private Long bugId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}