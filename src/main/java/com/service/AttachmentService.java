package com.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.entity.Attachment;

public interface AttachmentService {

    void uploadAttachment(MultipartFile file, Long bugId);

    Attachment getAttachment(Long id);

    void deleteAttachment(Long id);
    List<Attachment> getAttachmentsByBug(Long bugId);
}