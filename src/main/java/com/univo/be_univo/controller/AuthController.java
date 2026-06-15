package com.univo.be_univo.controller;

import com.univo.be_univo.dto.auth.AuthResponse;
import com.univo.be_univo.dto.auth.ForgotPasswordRequest;
import com.univo.be_univo.dto.auth.ForgotPasswordPhoneRequest;
import com.univo.be_univo.dto.auth.LoginRequest;
import com.univo.be_univo.dto.auth.ResetPasswordRequest;
import com.univo.be_univo.dto.auth.ResetPasswordWithOtpRequest;
import com.univo.be_univo.dto.user.CreateUserRequest;
import com.univo.be_univo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody CreateUserRequest request) {
        try {
            return authService.register(request);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        try {
            return authService.login(request);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, exception.getMessage(), exception);
        }
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
    }

    @PostMapping("/forgot-password/phone")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgotPasswordByPhone(@Valid @RequestBody ForgotPasswordPhoneRequest request) {
        authService.forgotPasswordByPhone(request);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @PostMapping("/reset-password/otp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPasswordWithOtp(@Valid @RequestBody ResetPasswordWithOtpRequest request) {
        try {
            authService.resetPasswordWithOtp(request);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }
}
