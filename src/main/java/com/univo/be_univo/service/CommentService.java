package com.univo.be_univo.service;

import com.univo.be_univo.dto.comment.CommentResponse;
import com.univo.be_univo.dto.comment.CreateCommentRequest;
import com.univo.be_univo.dto.comment.UpdateCommentRequest;
import com.univo.be_univo.entity.Comment;
import com.univo.be_univo.entity.Post;
import com.univo.be_univo.entity.User;
import com.univo.be_univo.repository.CommentRepository;
import com.univo.be_univo.repository.PostRepository;
import com.univo.be_univo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    private static final byte ACTIVE_POST_STATUS = 1;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<CommentResponse> getCommentsByPostId(UUID postId, boolean rootOnly) {
        List<Comment> comments = rootOnly
                ? commentRepository.findByPostIdAndCommentIsNullAndStatusTrue(postId)
                : commentRepository.findByPostIdAndStatusTrue(postId);

        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(this::toResponse)
                .toList();
    }

    public List<CommentResponse> getReplies(UUID parentCommentId) {
        return commentRepository.findByCommentIdAndStatusTrue(parentCommentId)
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(this::toResponse)
                .toList();
    }

    public Optional<CommentResponse> getCommentById(UUID id) {
        return commentRepository.findByIdAndStatusTrue(id)
                .map(this::toResponse);
    }

    public CommentResponse createComment(CreateCommentRequest request) {
        Post post = postRepository.findByIdAndStatus(request.postId(), ACTIVE_POST_STATUS)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findByIdAndStatusTrue(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment parentComment = null;
        if (request.parentCommentId() != null) {
            parentComment = commentRepository.findByIdAndStatusTrue(request.parentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));

            if (!parentComment.getPost().getId().equals(post.getId())) {
                throw new IllegalArgumentException("Parent comment does not belong to post");
            }
        }

        Date now = new Date();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setComment(parentComment);
        comment.setContent(request.content().trim());
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);
        comment.setStatus(true);

        return toResponse(commentRepository.save(comment));
    }

    public Optional<CommentResponse> updateComment(UUID id, UpdateCommentRequest request) {
        return commentRepository.findByIdAndStatusTrue(id)
                .map(comment -> {
                    comment.setContent(request.content().trim());
                    comment.setUpdatedAt(new Date());
                    return toResponse(commentRepository.save(comment));
                });
    }

    public boolean deleteComment(UUID id) {
        return commentRepository.findByIdAndStatusTrue(id)
                .map(comment -> {
                    comment.setStatus(false);
                    comment.setUpdatedAt(new Date());
                    commentRepository.save(comment);
                    return true;
                })
                .orElse(false);
    }

    private CommentResponse toResponse(Comment comment) {
        UUID parentCommentId = comment.getComment() == null ? null : comment.getComment().getId();

        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                parentCommentId,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getContent(),
                comment.isStatus()
        );
    }
}
