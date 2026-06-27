package com.univo.be_univo.service;

import com.univo.be_univo.dto.post.CreatePostRequest;
import com.univo.be_univo.dto.post.PostResponse;
import com.univo.be_univo.dto.post.UpdatePostRequest;
import com.univo.be_univo.entity.Post;
import com.univo.be_univo.entity.Topic;
import com.univo.be_univo.entity.User;
import com.univo.be_univo.repository.PostRepository;
import com.univo.be_univo.repository.TopicRepository;
import com.univo.be_univo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {
    private static final byte ACTIVE_STATUS = 1;
    private static final byte INACTIVE_STATUS = 0;

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public PostService(
            PostRepository postRepository,
            TopicRepository topicRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findByStatus(ACTIVE_STATUS)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<PostResponse> getPostById(UUID id) {
        return postRepository.findByIdAndStatus(id, ACTIVE_STATUS)
                .map(this::toResponse);
    }

    public List<PostResponse> getPostsByTopicId(UUID topicId) {
        return postRepository.findByTopicIdAndStatus(topicId, ACTIVE_STATUS)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PostResponse> getPostsByUserId(UUID userId) {
        return postRepository.findByUserIdAndStatus(userId, ACTIVE_STATUS)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PostResponse createPost(CreatePostRequest request) {
        Topic topic = topicRepository.findByIdAndStatusTrue(request.topicId())
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));
        User user = userRepository.findByIdAndStatusTrue(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Date now = new Date();
        Post post = new Post();
        post.setTopic(topic);
        post.setUser(user);
        post.setContent(request.content());
        post.setImages(request.images());
        post.setTags(request.tags());
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        post.setUpvote(0);
        post.setDownvote(0);
        post.setStatus(ACTIVE_STATUS);

        return toResponse(postRepository.save(post));
    }

    public Optional<PostResponse> updatePost(UUID id, UpdatePostRequest request) {
        return postRepository.findByIdAndStatus(id, ACTIVE_STATUS)
                .map(post -> {
                    if (request.topicId() != null) {
                        Topic topic = topicRepository.findByIdAndStatusTrue(request.topicId())
                                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));
                        post.setTopic(topic);
                    }

                    if (request.content() != null) {
                        post.setContent(request.content());
                    }

                    if (request.images() != null) {
                        post.setImages(request.images());
                    }

                    if (request.tags() != null) {
                        post.setTags(request.tags());
                    }

                    if (request.status() != null) {
                        post.setStatus(request.status());
                    }

                    post.setUpdatedAt(new Date());
                    return toResponse(postRepository.save(post));
                });
    }

    public boolean deletePost(UUID id) {
        return postRepository.findByIdAndStatus(id, ACTIVE_STATUS)
                .map(post -> {
                    post.setStatus(INACTIVE_STATUS);
                    post.setUpdatedAt(new Date());
                    postRepository.save(post);
                    return true;
                })
                .orElse(false);
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTopic().getId(),
                post.getTopic().getTopicName(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getContent(),
                post.getImages(),
                post.getUpvote(),
                post.getDownvote(),
                post.getStatus(),
                post.getTags()
        );
    }
}
