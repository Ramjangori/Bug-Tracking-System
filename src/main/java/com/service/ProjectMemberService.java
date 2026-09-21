package com.service;

import java.util.List;

import com.dto.UserResponse;

public interface ProjectMemberService {

    void addMember(Long projectId, Long userId);
    public UserResponse getMemberById(Long projectId, Long userId);
    public List<UserResponse> getAllMembers(Long projectId);
    public void deleteMember(Long projectId, Long userId);
    
}