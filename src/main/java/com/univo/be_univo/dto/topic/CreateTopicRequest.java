package com.univo.be_univo.dto.topic;

import jakarta.validation.constraints.NotBlank;

public record CreateTopicRequest(
        @NotBlank
        String topicName
) {
}
