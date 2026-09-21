package com.dto;

import java.time.LocalDate;

import com.enums.ProjectStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {

    @NotBlank(message = "Project name is required")
    private String name;

    @NotBlank(message = "Project description is required")
    private String description;
    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    @NotNull(message = "Start date is required")
    private LocalDate startsDate;


    private LocalDate endsDate;
    
}