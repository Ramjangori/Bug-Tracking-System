package com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotBlank(message = "Comment message is required")
    private String message;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Bug id is required")
    private Long bugId;
}