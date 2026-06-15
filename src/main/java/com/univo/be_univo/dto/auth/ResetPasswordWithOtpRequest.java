package com.univo.be_univo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordWithOtpRequest(
        @NotBlank
        String phoneNumber,

        @NotBlank
        @Pattern(regexp = "^\\d{6}$")
        String otp,

        @NotBlank
        @Size(min = 8)
        String newPassword
) {
}
