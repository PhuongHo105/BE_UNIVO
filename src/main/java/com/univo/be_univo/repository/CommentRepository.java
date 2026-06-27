package com.univo.be_univo.repository;

import com.univo.be_univo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Optional<Comment> findByIdAndStatusTrue(UUID id);
    List<Comment> findByPostIdAndStatusTrue(UUID postId);
    List<Comment> findByPostIdAndCommentIsNullAndStatusTrue(UUID postId);
    List<Comment> findByCommentIdAndStatusTrue(UUID commentId);
}
