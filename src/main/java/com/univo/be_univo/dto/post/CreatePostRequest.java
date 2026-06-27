package com.univo.be_univo.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreatePostRequest(
        @NotNull
        UUID topicId,

        @NotNull
        UUID userId,

        @NotBlank
        String content,

        List<String> images,

        List<String> tags
) {
}
