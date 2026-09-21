package com.dto;

import com.enums.BugPriority;
import com.enums.BugStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugRequest {

    @NotBlank(message = "Bug title is required")
    private String title;

    @NotBlank(message = "Bug description is required")
    private String description;

    @NotNull(message = "Bug priority is required")
    private BugPriority priority;

    @NotNull(message = "Bug status is required")
    private BugStatus status;

    @NotNull(message = "Project id is required")
    private Long projectId;


}