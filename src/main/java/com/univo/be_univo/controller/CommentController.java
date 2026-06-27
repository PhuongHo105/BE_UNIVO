package com.univo.be_univo.controller;

import com.univo.be_univo.dto.comment.CommentResponse;
import com.univo.be_univo.dto.comment.CreateCommentRequest;
import com.univo.be_univo.dto.comment.UpdateCommentRequest;
import com.univo.be_univo.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getComments(
            @RequestParam(required = false) UUID postId,
            @RequestParam(required = false) UUID parentCommentId,
            @RequestParam(defaultValue = "false") boolean rootOnly
    ) {
        if (parentCommentId != null) {
            return commentService.getReplies(parentCommentId);
        }

        if (postId != null) {
            return commentService.getCommentsByPostId(postId, rootOnly);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "postId or parentCommentId is required");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable UUID id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CreateCommentRequest request) {
        try {
            CommentResponse response = commentService.createComment(request);
            return ResponseEntity
                    .created(URI.create("/api/comments/" + response.id()))
                    .body(response);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        return commentService.updateComment(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        if (commentService.deleteComment(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
