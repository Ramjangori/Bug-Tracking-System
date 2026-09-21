package com.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.dto.UserRequest;
import com.dto.UserResponse;
import com.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
    
    List<UserResponse> toResponse(List<User> list);

	
}
