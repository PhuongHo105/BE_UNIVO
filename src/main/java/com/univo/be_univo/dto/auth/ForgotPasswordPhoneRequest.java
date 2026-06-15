package com.univo.be_univo.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordPhoneRequest(
        @NotBlank
        String phoneNumber
) {
}
