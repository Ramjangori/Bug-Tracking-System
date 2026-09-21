package com.service.impl;

import com.entity.Attachment;
import com.entity.Bug;
import com.entity.User;
import com.exception.AttachmentNotFoundException;
import com.exception.BugNotFoundException;
import com.exception.UserNotFoundException;
import com.repository.AttachmentRepository;
import com.repository.BugRepository;
import com.repository.UserRepository;
import com.service.AttachmentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final BugRepository bugRepository;
    private final String uploadDirectory = "uploads/attachments/";
    private final UserRepository userRepository;

    public AttachmentServiceImpl( UserRepository userRepository , AttachmentRepository attachmentRepository, BugRepository bugRepository) {
        this.attachmentRepository = attachmentRepository;
        this.bugRepository = bugRepository;
        this.userRepository=userRepository;
    }


    @Override
    public void uploadAttachment(MultipartFile file, Long bugId) {

        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() ->
                        new BugNotFoundException(
                                "Bug with id " + bugId + " not found."));

        // find user
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found."));

        try{
            Path uploadPath = Paths.get(uploadDirectory);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            // 3. Get original file name
            String originalFileName = file.getOriginalFilename();

            // 4. Generate unique file name
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            // 5. Create complete file path
            Path filePath = uploadPath.resolve(fileName);

            // 6. Save actual file to disk
            Files.copy(file.getInputStream(), filePath);

            // 7. Create Attachment entity
            Attachment attachment = new Attachment();

            attachment.setFileName(originalFileName);
            attachment.setFileType(file.getContentType());
            attachment.setFilePath(filePath.toString());
            attachment.setFileSize(file.getSize());
            attachment.setBug(bug);
            attachment.setUploadedAt(LocalDateTime.now());
            attachment.setUploadedBy(user);

            // 8. Save attachment details in database
            attachmentRepository.save(attachment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Attachment getAttachment(Long id) {

        return attachmentRepository.findById(id)
                .orElseThrow(() ->
                        new AttachmentNotFoundException(
                                "Attachment with id " + id + " not found."));
    }

    @Override
    public void deleteAttachment(Long id) {

        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() ->
                        new AttachmentNotFoundException(
                                "Attachment with id " + id + " not found."));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String currentEmail = authentication.getName();

        String ownerEmail = attachment.getUploadedBy().getEmail();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = currentEmail.equals(ownerEmail);

        // Neither ADMIN nor owner
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException(
                    "You can delete only your own attachment.");
        }

        try {

            Path filePath = Paths.get(attachment.getFilePath());

            Files.deleteIfExists(filePath);

            attachmentRepository.delete(attachment);

        } catch (IOException e) {

            throw new RuntimeException("Failed to delete file.", e);
        }
    }
}
