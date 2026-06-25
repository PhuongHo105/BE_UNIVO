package com.univo.be_univo.repository;

import com.univo.be_univo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByStatusTrue();
    Optional<User> findByIdAndStatusTrue(UUID id);
    Optional<User> findByUidAndStatusTrue(String uid);
    Optional<User> findByEmailAndStatusTrue(String email);
    Optional<User> findByPhoneNumberAndStatusTrue(String phoneNumber);
    Optional<User> findByPasswordResetTokenAndStatusTrue(String passwordResetToken);
    boolean existsByUid(String uid);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUidAndIdNot(String uid, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, UUID id);
}
