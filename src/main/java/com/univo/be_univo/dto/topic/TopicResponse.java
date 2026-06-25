package com.univo.be_univo.dto.topic;

import java.util.UUID;

public record TopicResponse(
        UUID id,
        String topicName
) {
}
