package com.univo.be_univo.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCommentRequest(
        @NotNull
        UUID postId,

        @NotNull
        UUID userId,

        UUID parentCommentId,

        @NotBlank
        String content
) {
}
