package com.univo.be_univo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 3, max = 30)
        @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9._]*$")
        String uid,

        @Email
        String email,

        String name,

        String phoneNumber,

        Boolean status
) {
}
