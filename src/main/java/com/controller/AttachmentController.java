package com.controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.entity.Attachment;
import com.service.AttachmentService;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getAttachment(
            @PathVariable Long id) throws java.io.IOException {

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

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttachment(
            @PathVariable Long id) {

        attachmentService.deleteAttachment(id);

        return ResponseEntity
                .ok()
                .body("Attachment deleted successfully.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/bug/{bugId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByBug(
            @PathVariable Long bugId) {

        List<Attachment> attachments =
                attachmentService.getAttachmentsByBug(bugId);

        return ResponseEntity.ok(attachments);
    }
}