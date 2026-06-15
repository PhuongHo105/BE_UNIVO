package com.univo.be_univo.repository;

import com.univo.be_univo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUid(String uid);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUidAndIdNot(String uid, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, UUID id);
}
