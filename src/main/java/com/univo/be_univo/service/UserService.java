package com.univo.be_univo.service;

import com.univo.be_univo.dto.user.AddUserPointRequest;
import com.univo.be_univo.dto.user.CreateUserRequest;
import com.univo.be_univo.dto.user.UpdateUserRequest;
import com.univo.be_univo.dto.user.UserResponse;
import com.univo.be_univo.entity.User;
import com.univo.be_univo.enums.UserBadge;
import com.univo.be_univo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<UserResponse> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::toResponse);
    }

    public UserResponse createUser(CreateUserRequest request) {
        String uid = normalizeUid(request.uid());

        if (userRepository.existsByUid(uid)) {
            throw new IllegalArgumentException("Uid already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        User user = new User();
        user.setUid(uid);
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setPhoneNumber(request.phoneNumber());
        user.setBadge(UserBadge.Newcomer);
        user.setPoint(0);
        user.setLevel((byte) 1);
        user.setStatus(true);

        return toResponse(userRepository.save(user));
    }

    public Optional<UserResponse> updateUser(UUID id, UpdateUserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (request.uid() != null) {
                        String uid = normalizeUid(request.uid());
                        if (userRepository.existsByUidAndIdNot(uid, id)) {
                            throw new IllegalArgumentException("Uid already exists");
                        }
                        user.setUid(uid);
                    }

                    if (request.email() != null) {
                        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
                            throw new IllegalArgumentException("Email already exists");
                        }
                        user.setEmail(request.email());
                    }

                    if (request.name() != null) {
                        user.setName(request.name());
                    }

                    if (request.phoneNumber() != null) {
                        if (userRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), id)) {
                            throw new IllegalArgumentException("Phone number already exists");
                        }
                        user.setPhoneNumber(request.phoneNumber());
                    }

                    if (request.status() != null) {
                        user.setStatus(request.status());
                    }

                    return toResponse(userRepository.save(user));
                });
    }

    private String normalizeUid(String uid) {
        return uid.trim().toLowerCase(Locale.ROOT);
    }

    public Optional<UserResponse> addPoint(UUID id, AddUserPointRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    long totalPoint = (long) user.getPoint() + request.point();
                    if (totalPoint > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException("Point is too large");
                    }

                    user.setPoint((int) totalPoint);
                    user.setLevel(calculateLevel((int) totalPoint));
                    user.setBadge(calculateBadge((int) totalPoint));

                    return toResponse(userRepository.save(user));
                });
    }

    private byte calculateLevel(int point) {
        return (byte) Math.min(127, point / 100 + 1);
    }

    private UserBadge calculateBadge(int point) {
        if (point >= 10000) {
            return UserBadge.Legend;
        }
        if (point >= 5000) {
            return UserBadge.CommunityHero;
        }
        if (point >= 3500) {
            return UserBadge.RisingStar;
        }
        if (point >= 2000) {
            return UserBadge.Contributor;
        }
        if (point >= 1000) {
            return UserBadge.Supporter;
        }
        if (point >= 500) {
            return UserBadge.CommunityVoice;
        }
        if (point >= 250) {
            return UserBadge.Enthusiast;
        }
        if (point >= 100) {
            return UserBadge.ActiveMember;
        }
        if (point >= 50) {
            return UserBadge.EarlyAdopter;
        }
        return UserBadge.Newcomer;
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
