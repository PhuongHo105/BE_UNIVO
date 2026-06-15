package com.univo.be_univo.dto.auth;

import com.univo.be_univo.dto.user.UserResponse;

public record AuthResponse(
        UserResponse user
) {
}
