package com.syslogin.repository;

import com.syslogin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> getByResetPasswordToken(String token);
    Optional<User> getByUsername(String username);
}
