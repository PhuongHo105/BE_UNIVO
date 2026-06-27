package com.univo.be_univo.dto.post;

import java.util.List;
import java.util.UUID;

public record UpdatePostRequest(
        UUID topicId,
        String content,
        List<String> images,
        List<String> tags,
        Byte status
) {
}
