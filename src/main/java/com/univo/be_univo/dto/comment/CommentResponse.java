package com.univo.be_univo.dto.comment;

import java.util.Date;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        UUID postId,
        UUID userId,
        String userName,
        UUID parentCommentId,
        Date createdAt,
        Date updatedAt,
        String content,
        boolean status
) {
}
