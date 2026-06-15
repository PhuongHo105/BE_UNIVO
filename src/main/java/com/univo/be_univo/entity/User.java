package com.univo.be_univo.entity;

import com.univo.be_univo.enums.UserBadge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false, length = 30)
    private String uid;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserBadge badge;
    private int point;
    private byte level;
    private boolean status;
    private String passwordResetToken;
    private Instant passwordResetTokenExpiresAt;
    private String passwordResetOtp;
    private Instant passwordResetOtpExpiresAt;
}
