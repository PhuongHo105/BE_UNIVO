package com.univo.be_univo.repository;

import com.univo.be_univo.entity.TopicFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TopicFollowRepository extends JpaRepository<TopicFollow, UUID> {
}
