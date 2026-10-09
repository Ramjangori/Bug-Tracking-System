package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
  
	List<Attachment> findByBug_Id(Long bugId);
}
