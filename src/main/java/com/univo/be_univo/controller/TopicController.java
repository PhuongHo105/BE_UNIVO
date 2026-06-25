package com.univo.be_univo.controller;

import com.univo.be_univo.dto.topic.CreateTopicRequest;
import com.univo.be_univo.dto.topic.TopicResponse;
import com.univo.be_univo.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<TopicResponse> getTopics(@RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return topicService.getAllTopics();
        }

        return topicService.findTopics(keyword);
    }

    @PostMapping
    public ResponseEntity<TopicResponse> createTopic(@Valid @RequestBody CreateTopicRequest request) {
        TopicResponse response = topicService.createTopic(request.topicName());
        return ResponseEntity
                .created(URI.create("/api/topics/" + response.id()))
                .body(response);
    }
}
