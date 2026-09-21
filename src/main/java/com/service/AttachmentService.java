package com.service;

import com.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    void uploadAttachment(MultipartFile file, Long bugId);

    Attachment getAttachment(Long id);

    void deleteAttachment(Long id);
}