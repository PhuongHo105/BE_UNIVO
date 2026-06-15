package com.univo.be_univo.service;

import com.univo.be_univo.dto.auth.AuthResponse;
import com.univo.be_univo.dto.auth.ForgotPasswordRequest;
import com.univo.be_univo.dto.auth.ForgotPasswordPhoneRequest;
import com.univo.be_univo.dto.auth.LoginRequest;
import com.univo.be_univo.dto.auth.ResetPasswordRequest;
import com.univo.be_univo.dto.auth.ResetPasswordWithOtpRequest;
import com.univo.be_univo.dto.user.CreateUserRequest;
import com.univo.be_univo.dto.user.UserResponse;
import com.univo.be_univo.entity.User;
import com.univo.be_univo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SmsService smsService;
    private final String passwordResetUrl;
    private final Random random = new Random();

    public AuthService(
            UserRepository userRepository,
            UserService userService,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            SmsService smsService,
            @Value("${app.password-reset-url}") String passwordResetUrl
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.smsService = smsService;
        this.passwordResetUrl = passwordResetUrl;
    }

    public AuthResponse register(CreateUserRequest request) {
        return new AuthResponse(userService.createUser(request));
    }

    public AuthResponse login(LoginRequest request) {
        User user = findUserByIdentifier(request.identifier());

        if (!user.isStatus()) {
            throw new BadCredentialsException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new AuthResponse(toResponse(user));
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresentOrElse(user -> {
                    String token = UUID.randomUUID().toString();
                    user.setPasswordResetToken(hashToken(token));
                    user.setPasswordResetTokenExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
                    userRepository.save(user);

                    emailService.sendPasswordResetEmail(user.getEmail(), passwordResetUrl + "?token=" + token);
                }, () -> LOGGER.warn("Password reset requested for non-existing email {}", request.email()));
    }

    public void forgotPasswordByPhone(ForgotPasswordPhoneRequest request) {
        userRepository.findByPhoneNumber(request.phoneNumber())
                .ifPresent(user -> {
                    String otp = generateOtp();
                    user.setPasswordResetOtp(hashToken(otp));
                    user.setPasswordResetOtpExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));
                    userRepository.save(user);

                    smsService.sendPasswordResetOtp(user.getPhoneNumber(), otp);
                });
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByPasswordResetToken(hashToken(request.token()))
                .orElseThrow(() -> new BadCredentialsException("Invalid reset token"));

        if (user.getPasswordResetTokenExpiresAt() == null
                || user.getPasswordResetTokenExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("Reset token expired");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        userRepository.save(user);
    }

    public void resetPasswordWithOtp(ResetPasswordWithOtpRequest request) {
        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new BadCredentialsException("Invalid OTP"));

        if (user.getPasswordResetOtp() == null
                || user.getPasswordResetOtpExpiresAt() == null
                || user.getPasswordResetOtpExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("OTP expired");
        }

        if (!user.getPasswordResetOtp().equals(hashToken(request.otp()))) {
            throw new BadCredentialsException("Invalid OTP");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setPasswordResetOtp(null);
        user.setPasswordResetOtpExpiresAt(null);
        userRepository.save(user);
    }

    private User findUserByIdentifier(String identifier) {
        String value = identifier.trim();
        String uid = value.toLowerCase(Locale.ROOT);

        return userRepository.findByEmail(value)
                .or(() -> userRepository.findByPhoneNumber(value))
                .or(() -> userRepository.findByUid(uid))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private String generateOtp() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUid(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getBadge(),
                user.getPoint(),
                user.getLevel(),
                user.isStatus()
        );
    }
}
