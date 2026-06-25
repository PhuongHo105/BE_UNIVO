package com.univo.be_univo.repository;

import com.univo.be_univo.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
    List<Topic> findByStatusTrue();
    Optional<Topic> findByTopicNameAndStatusTrue(String topicName);
}
