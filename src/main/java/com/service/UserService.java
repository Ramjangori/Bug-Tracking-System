package com.service;

import java.util.List;

import com.dto.UserRequest;
import com.dto.UserResponse;
import com.entity.User;

public interface UserService {
 
    UserResponse registerUser(UserRequest user);

   
    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

   
    UserResponse updateUser(Long id, UserRequest user);
    
    UserResponse getMyProfile();

    
    void deleteUser(Long id);
}
