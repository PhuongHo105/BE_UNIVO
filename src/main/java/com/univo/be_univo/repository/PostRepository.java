package com.univo.be_univo.repository;

import com.univo.be_univo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByStatus(byte status);
    Optional<Post> findByIdAndStatus(UUID id, byte status);
    List<Post> findByTopicIdAndStatus(UUID topicId, byte status);
    List<Post> findByUserIdAndStatus(UUID userId, byte status);
}
