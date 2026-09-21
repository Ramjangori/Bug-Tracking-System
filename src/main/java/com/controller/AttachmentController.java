package com.controller;

import com.entity.Attachment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.service.AttachmentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    // Upload file
    @PreAuthorize("hasAnyRole('DEVELOPER', 'TESTER')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bugId") Long bugId) {

        attachmentService.uploadAttachment(file, bugId);

        return ResponseEntity
                .status(201)
                .body("File uploaded successfully.");
    }

    // Get Attachment
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getAttachment(
            @PathVariable Long id) {

        Attachment attachment = attachmentService.getAttachment(id);

        try {

            Path filePath = Paths.get(attachment.getFilePath());

            byte[] file = Files.readAllBytes(filePath);

            MediaType mediaType =
                    MediaType.parseMediaType(attachment.getFileType());

            return ResponseEntity
                    .ok()
                    .contentType(mediaType)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + attachment.getFileName() + "\""
                    )
                    .body(file);

        } catch (IOException e) {

            throw new RuntimeException("Failed to read file.", e);
        }
    }

    // Delete file
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttachment(
            @PathVariable Long id) {

        attachmentService.deleteAttachment(id);

        return ResponseEntity
                .ok()
                .body("Attachment deleted successfully.");
    }
}
