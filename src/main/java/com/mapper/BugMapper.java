package com.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dto.BugRequest;
import com.dto.BugResponse;
import com.entity.Bug;

@Mapper(componentModel = "spring")
public interface BugMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    Bug toEntity(BugRequest request);

    // Entity → Response
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.firstName")
    @Mapping(target = "assignedToId", source = "assignedTo.id")
    @Mapping(target = "assignedToName", source = "assignedTo.firstName")
    BugResponse toResponse(Bug bug);

    // List<Entity> → List<Response>
    List<BugResponse> toResponse(List<Bug> bugs);
}