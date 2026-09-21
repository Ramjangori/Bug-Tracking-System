package com.dto;

import java.time.LocalDate;

import com.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate startsDate;
    private LocalDate endsDate;
    private Long createdById;
    private String createdByName;

}