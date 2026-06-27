package com.univo.be_univo.dto.post;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record PostResponse(
        UUID id,
        UUID topicId,
        String topicName,
        UUID userId,
        String userName,
        Date createdAt,
        Date updatedAt,
        String content,
        List<String> images,
        int upvote,
        int downvote,
        byte status,
        List<String> tags
) {
}
