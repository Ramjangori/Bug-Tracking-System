package com.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dto.ProjectRequest;
import com.dto.ProjectResponse;
import com.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "createdBy", ignore = true)
    Project toEntity(ProjectRequest request);

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.firstName")
    ProjectResponse toResponse(Project project);

    List<ProjectResponse> toResponse(List<Project> projects);

}