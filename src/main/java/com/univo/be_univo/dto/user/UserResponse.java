package com.univo.be_univo.dto.user;

import com.univo.be_univo.enums.UserBadge;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String uid,
        String email,
        String name,
        String phoneNumber,
        UserBadge badge,
        int point,
        byte level,
        boolean status
) {
}
