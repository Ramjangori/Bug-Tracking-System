package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
