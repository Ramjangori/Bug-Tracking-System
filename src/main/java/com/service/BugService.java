package com.service;

import com.dto.BugRequest;
import com.dto.BugResponse;

import java.util.List;

public interface BugService {

    BugResponse createBug(BugRequest request);

    BugResponse getBugById(Long id);

    List<BugResponse> getAllBugs();

    BugResponse updateBug(Long id, BugRequest request);

    void deleteBug(Long id);
    BugResponse assignBug(Long bugId, Long developerId);
}