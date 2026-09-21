package com.dto;

import com.enums.BugPriority;
import com.enums.BugStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugResponse {

    private Long id;

    private String title;

    private String description;

    private BugPriority priority;

    private BugStatus status;

    private Long projectId;

    private String projectName;

    private Long assignedToId;

    private String assignedToName;

    private Long createdById;

    private String createdByName;
}