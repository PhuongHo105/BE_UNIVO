package com.univo.be_univo.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
        @NotBlank
        String content
) {
}
