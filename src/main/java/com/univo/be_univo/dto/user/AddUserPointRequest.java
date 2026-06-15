package com.univo.be_univo.dto.user;

import jakarta.validation.constraints.Positive;

public record AddUserPointRequest(
        @Positive
        int point
) {
}
