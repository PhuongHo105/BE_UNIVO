package com.univo.be_univo.service;

import com.univo.be_univo.dto.topic.TopicResponse;
import com.univo.be_univo.entity.Topic;
import com.univo.be_univo.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<TopicResponse> getAllTopics() {
        return topicRepository.findByStatusTrue().stream().map(this::toResponse).toList();
    }

    public List<TopicResponse> findTopics(String keyword) {
        String normalizedKeyword = normalize(keyword);

        return topicRepository.findByStatusTrue()
                .stream()
                .filter(topic -> normalize(topic.getTopicName()).contains(normalizedKeyword))
                .map(this::toResponse)
                .toList();
    }

    public TopicResponse createTopic(String topicName) {
        String value = topicName.trim();

        return topicRepository.findByTopicNameAndStatusTrue(value)
                .map(this::toResponse)
                .orElseGet(() -> {
                    Topic topic = new Topic();
                    topic.setTopicName(value);
                    topic.setStatus(true);
                    return toResponse(topicRepository.save(topic));
                });
    }

    private String normalize(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    private TopicResponse toResponse(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getTopicName()
        );
    }
}
