package com.univo.be_univo.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String identifier,

        @NotBlank
        String password
) {
}
